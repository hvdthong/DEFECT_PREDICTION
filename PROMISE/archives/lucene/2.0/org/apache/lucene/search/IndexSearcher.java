import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

/** Implements search over a single IndexReader.
 *
 * <p>Applications usually need only call the inherited {@link #search(Query)}
 * or {@link #search(Query,Filter)} methods. For performance reasons it is 
 * recommended to open only one IndexSearcher and use it for all of your searches.
 * 
 * <p>Note that you can only access Hits from an IndexSearcher as long as it is
 * not yet closed, otherwise an IOException will be thrown. 
 */
public class IndexSearcher extends Searcher {
  IndexReader reader;
  private boolean closeReader;

  /** Creates a searcher searching the index in the named directory. */
  public IndexSearcher(String path) throws IOException {
    this(IndexReader.open(path), true);
  }

  /** Creates a searcher searching the index in the provided directory. */
  public IndexSearcher(Directory directory) throws IOException {
    this(IndexReader.open(directory), true);
  }

  /** Creates a searcher searching the provided index. */
  public IndexSearcher(IndexReader r) {
    this(r, false);
  }
  
  private IndexSearcher(IndexReader r, boolean closeReader) {
    reader = r;
    this.closeReader = closeReader;
  }

  /** Return the {@link IndexReader} this searches. */
  public IndexReader getIndexReader() {
    return reader;
  }

  /**
   * Note that the underlying IndexReader is not closed, if
   * IndexSearcher was constructed with IndexSearcher(IndexReader r).
   * If the IndexReader was supplied implicitly by specifying a directory, then
   * the IndexReader gets closed.
   */
  public void close() throws IOException {
    if(closeReader)
      reader.close();
  }

  public int docFreq(Term term) throws IOException {
    return reader.docFreq(term);
  }

  public Document doc(int i) throws IOException {
    return reader.document(i);
  }

  public int maxDoc() throws IOException {
    return reader.maxDoc();
  }

  public TopDocs search(Weight weight, Filter filter, final int nDocs)
       throws IOException {

      throw new IllegalArgumentException("nDocs must be > 0");

    TopDocCollector collector = new TopDocCollector(nDocs);
    search(weight, filter, collector);
    return collector.topDocs();
  }

  public TopFieldDocs search(Weight weight, Filter filter, final int nDocs,
                             Sort sort)
      throws IOException {

    TopFieldDocCollector collector =
      new TopFieldDocCollector(reader, sort, nDocs);
    search(weight, filter, collector);
    return (TopFieldDocs)collector.topDocs();
  }

  public void search(Weight weight, Filter filter,
                     final HitCollector results) throws IOException {
    HitCollector collector = results;
    if (filter != null) {
      final BitSet bits = filter.bits(reader);
      collector = new HitCollector() {
          public final void collect(int doc, float score) {
              results.collect(doc, score);
            }
          }
        };
    }

    Scorer scorer = weight.scorer(reader);
    if (scorer == null)
      return;
    scorer.score(collector);
  }

  public Query rewrite(Query original) throws IOException {
    Query query = original;
    for (Query rewrittenQuery = query.rewrite(reader); rewrittenQuery != query;
         rewrittenQuery = query.rewrite(reader)) {
      query = rewrittenQuery;
    }
    return query;
  }

  public Explanation explain(Weight weight, int doc) throws IOException {
    return weight.explain(reader, doc);
  }
}
