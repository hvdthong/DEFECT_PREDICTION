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

  private ScoreDoc reusableSD;
  
  /** The total number of hits the collector encountered. */
  protected int totalHits;
  
  /** The priority queue which holds the top-scoring documents. */
  protected PriorityQueue hq;
    
  /** Construct to collect a given number of hits.
   * @param numHits the maximum number of hits to collect
   */
  public TopDocCollector(int numHits) {
    this(new HitQueue(numHits));
  }

  /** @deprecated use TopDocCollector(hq) instead. numHits is not used by this
   * constructor. It will be removed in a future release.
   */
  TopDocCollector(int numHits, PriorityQueue hq) {
    this.hq = hq;
  }

  /** Constructor to collect the top-scoring documents by using the given PQ.
   * @param hq the PQ to use by this instance.
   */
  protected TopDocCollector(PriorityQueue hq) {
    this.hq = hq;
  }

  public void collect(int doc, float score) {
    if (score > 0.0f) {
      totalHits++;
      if (reusableSD == null) {
        reusableSD = new ScoreDoc(doc, score);
      } else if (score >= reusableSD.score) {
        reusableSD.doc = doc;
        reusableSD.score = score;
      } else {
        return;
      }
      reusableSD = (ScoreDoc) hq.insertWithOverflow(reusableSD);
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
