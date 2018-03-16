import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.util.PriorityQueue;

/** Implements parallel search over a set of <code>Searchables</code>.
 *
 * <p>Applications usually need only call the inherited {@link #search(Query)}
 * or {@link #search(Query,Filter)} methods.
 */
public class ParallelMultiSearcher extends MultiSearcher {

  private Searchable[] searchables;
  private int[] starts;
	
  /** Creates a searcher which searches <i>searchables</i>. */
  public ParallelMultiSearcher(Searchable[] searchables) throws IOException {
    super(searchables);
    this.searchables=searchables;
    this.starts=getStarts();
  }

  /**
   * TODO: parallelize this one too
   */
  public int docFreq(Term term) throws IOException {
    return super.docFreq(term);
  }

  /**
   * A search implementation which spans a new thread for each
   * Searchable, waits for each search to complete and merge
   * the results back together.
   */
  public TopDocs search(Weight weight, Filter filter, int nDocs)
    throws IOException {
    HitQueue hq = new HitQueue(nDocs);
    int totalHits = 0;
    MultiSearcherThread[] msta =
      new MultiSearcherThread[searchables.length];
      msta[i] =
        new MultiSearcherThread(
                                searchables[i],
                                weight,
                                filter,
                                nDocs,
                                hq,
                                i,
                                starts,
                                "MultiSearcher thread #" + (i + 1));
      msta[i].start();
    }

    for (int i = 0; i < searchables.length; i++) {
      try {
        msta[i].join();
      } catch (InterruptedException ie) {
      }
      IOException ioe = msta[i].getIOException();
      if (ioe == null) {
        totalHits += msta[i].hits();
      } else {
        throw ioe;
      }
    }

    ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
      scoreDocs[i] = (ScoreDoc) hq.pop();

    float maxScore = (totalHits==0) ? Float.NEGATIVE_INFINITY : scoreDocs[0].score;
    
    return new TopDocs(totalHits, scoreDocs, maxScore);
  }

  /**
   * A search implementation allowing sorting which spans a new thread for each
   * Searchable, waits for each search to complete and merges
   * the results back together.
   */
  public TopFieldDocs search(Weight weight, Filter filter, int nDocs, Sort sort)
    throws IOException {
    FieldDocSortedHitQueue hq = new FieldDocSortedHitQueue (null, nDocs);
    int totalHits = 0;
    MultiSearcherThread[] msta = new MultiSearcherThread[searchables.length];
      msta[i] =
        new MultiSearcherThread(
                                searchables[i],
                                weight,
                                filter,
                                nDocs,
                                hq,
                                sort,
                                i,
                                starts,
                                "MultiSearcher thread #" + (i + 1));
      msta[i].start();
    }

    float maxScore=Float.NEGATIVE_INFINITY;
    
    for (int i = 0; i < searchables.length; i++) {
      try {
        msta[i].join();
      } catch (InterruptedException ie) {
      }
      IOException ioe = msta[i].getIOException();
      if (ioe == null) {
        totalHits += msta[i].hits();
        maxScore=Math.max(maxScore, msta[i].getMaxScore());
      } else {
        throw ioe;
      }
    }

    ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
      scoreDocs[i] = (ScoreDoc) hq.pop();

    return new TopFieldDocs(totalHits, scoreDocs, hq.getFields(), maxScore);
  }

  /** Lower-level search API.
   *
   * <p>{@link HitCollector#collect(int,float)} is called for every non-zero
   * scoring document.
   *
   * <p>Applications should only use this if they need <i>all</i> of the
   * matching documents.  The high-level search API ({@link
   * Searcher#search(Query)}) is usually more efficient, as it skips
   * non-high-scoring hits.
   *
   * @param weight to match documents
   * @param filter if non-null, a bitset used to eliminate some documents
   * @param results to receive hits
   * 
   * @todo parallelize this one too
   */
  public void search(Weight weight, Filter filter, final HitCollector results)
    throws IOException {
    for (int i = 0; i < searchables.length; i++) {

      final int start = starts[i];

      searchables[i].search(weight, filter, new HitCollector() {
          public void collect(int doc, float score) {
            results.collect(doc + start, score);
          }
        });

    }
  }

  /*
   * TODO: this one could be parallelized too
   * @see org.apache.lucene.search.Searchable#rewrite(org.apache.lucene.search.Query)
   */
  public Query rewrite(Query original) throws IOException {
    return super.rewrite(original);
  }

}

/**
 * A thread subclass for searching a single searchable 
 */
class MultiSearcherThread extends Thread {

  private Searchable searchable;
  private Weight weight;
  private Filter filter;
  private int nDocs;
  private TopDocs docs;
  private int i;
  private PriorityQueue hq;
  private int[] starts;
  private IOException ioe;
  private Sort sort;

  public MultiSearcherThread(
                             Searchable searchable,
                             Weight weight,
                             Filter filter,
                             int nDocs,
                             HitQueue hq,
                             int i,
                             int[] starts,
                             String name) {
    super(name);
    this.searchable = searchable;
    this.weight = weight;
    this.filter = filter;
    this.nDocs = nDocs;
    this.hq = hq;
    this.i = i;
    this.starts = starts;
  }

  public MultiSearcherThread(
                             Searchable searchable,
                             Weight weight,
                             Filter filter,
                             int nDocs,
                             FieldDocSortedHitQueue hq,
                             Sort sort,
                             int i,
                             int[] starts,
                             String name) {
    super(name);
    this.searchable = searchable;
    this.weight = weight;
    this.filter = filter;
    this.nDocs = nDocs;
    this.hq = hq;
    this.i = i;
    this.starts = starts;
    this.sort = sort;
  }

  public void run() {
    try {
      docs = (sort == null) ? searchable.search (weight, filter, nDocs)
        : searchable.search (weight, filter, nDocs, sort);
    }
    catch (IOException ioe) {
      this.ioe = ioe;
    }
    if (ioe == null) {
      if (sort != null) {
        ((FieldDocSortedHitQueue)hq).setFields (((TopFieldDocs)docs).fields);
      }
      ScoreDoc[] scoreDocs = docs.scoreDocs;
      for (int j = 0;
           j < scoreDocs.length;
        ScoreDoc scoreDoc = scoreDocs[j];
        synchronized (hq) {
          if (!hq.insert(scoreDoc))
            break;
      }
    }
  }

  public int hits() {
    return docs.totalHits;
  }

  public float getMaxScore() {
      return docs.getMaxScore();
  }
  
  public IOException getIOException() {
    return ioe;
  }

}
