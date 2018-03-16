public class ScoreDoc implements java.io.Serializable {
  /** Expert: The score of this document for the query. */
  public float score;

  /** Expert: A hit document's number.
   * @see Searcher#doc(int)
   */
  public int doc;

  /** Expert: Constructs a ScoreDoc. */
  public ScoreDoc(int doc, float score) {
    this.doc = doc;
    this.score = score;
  }
}
