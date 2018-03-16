public class TopDocs implements java.io.Serializable {
  /** Expert: The total number of hits for the query.
   * @see Hits#length()
  */
  public int totalHits;
  /** Expert: The top hits for the query. */
  public ScoreDoc[] scoreDocs;
  /** Expert: Stores the maximum score value encountered, needed for normalizing. */
  private float maxScore;
  
  /** Expert: Returns the maximum score value encountered. */
  public float getMaxScore() {
      return maxScore;
  }
  
  /** Expert: Sets the maximum score value encountered. */
  public void setMaxScore(float maxScore) {
      this.maxScore=maxScore;
  }
  
  /** Expert: Constructs a TopDocs.*/
  TopDocs(int totalHits, ScoreDoc[] scoreDocs, float maxScore) {
    this.totalHits = totalHits;
    this.scoreDocs = scoreDocs;
    this.maxScore = maxScore;
  }
}
