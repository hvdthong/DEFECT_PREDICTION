import java.io.IOException;

import org.apache.lucene.index.IndexReader;

/** A {@link HitCollector} implementation that collects the top-sorting
 * documents, returning them as a {@link TopFieldDocs}.  This is used by {@link
 * IndexSearcher} to implement {@link TopFieldDocs}-based search.
 *
 * <p>This may be extended, overriding the collect method to, e.g.,
 * conditionally invoke <code>super()</code> in order to filter which
 * documents are collected.
 **/
public class TopFieldDocCollector extends TopDocCollector {

  private FieldDoc reusableFD;

  /** Construct to collect a given number of hits.
   * @param reader the index to be searched
   * @param sort the sort criteria
   * @param numHits the maximum number of hits to collect
   */
  public TopFieldDocCollector(IndexReader reader, Sort sort, int numHits)
    throws IOException {
    super(new FieldSortedHitQueue(reader, sort.fields, numHits));
  }

  public void collect(int doc, float score) {
    if (score > 0.0f) {
      totalHits++;
      if (reusableFD == null)
        reusableFD = new FieldDoc(doc, score);
      else {
        reusableFD.score = score;
        reusableFD.doc = doc;
      }
      reusableFD = (FieldDoc) hq.insertWithOverflow(reusableFD);
    }
  }

  public TopDocs topDocs() {
    FieldSortedHitQueue fshq = (FieldSortedHitQueue)hq;
    ScoreDoc[] scoreDocs = new ScoreDoc[fshq.size()];
      scoreDocs[i] = fshq.fillFields ((FieldDoc) fshq.pop());

    return new TopFieldDocs(totalHits, scoreDocs,
                            fshq.getFields(), fshq.getMaxScore());
  }
}
