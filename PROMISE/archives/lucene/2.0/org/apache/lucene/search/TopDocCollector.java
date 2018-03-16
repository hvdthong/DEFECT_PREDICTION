import org.apache.lucene.util.PriorityQueue;

/** A {@link HitCollector} implementation that collects the top-scoring
 * documents, returning them as a {@link TopDocs}.  This is used by {@link
 * IndexSearcher} to implement {@link TopDocs}-based search.
 *
 * <p>This may be extended, overriding the collect method to, e.g.,
 * conditionally invoke <code>super()</code> in order to filter which
 * documents are collected.
 **/
public class TopDocCollector extends HitCollector {
  private int numHits;
  private float minScore = 0.0f;

  int totalHits;
  PriorityQueue hq;
    
  /** Construct to collect a given number of hits.
   * @param numHits the maximum number of hits to collect
   */
  public TopDocCollector(int numHits) {
    this(numHits, new HitQueue(numHits));
  }

  TopDocCollector(int numHits, PriorityQueue hq) {
    this.numHits = numHits;
    this.hq = hq;
  }

  public void collect(int doc, float score) {
    if (score > 0.0f) {
      totalHits++;
      if (hq.size() < numHits || score >= minScore) {
        hq.insert(new ScoreDoc(doc, score));
      }
    }
  }

  /** The total number of documents that matched this query. */
  public int getTotalHits() { return totalHits; }

  /** The top-scoring hits. */
  public TopDocs topDocs() {
    ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
      scoreDocs[i] = (ScoreDoc)hq.pop();
      
    float maxScore = (totalHits==0)
      ? Float.NEGATIVE_INFINITY
      : scoreDocs[0].score;
    
    return new TopDocs(totalHits, scoreDocs, maxScore);
  }
}
