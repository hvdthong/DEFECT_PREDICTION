public class SimilarityDelegator extends Similarity {

  private Similarity delegee;

  /** Construct a {@link Similarity} that delegates all methods to another.
   *
   * @param delegee the Similarity implementation to delegate to
   */
  public SimilarityDelegator(Similarity delegee) {
    this.delegee = delegee;
  }

  public float lengthNorm(String fieldName, int numTerms) {
    return delegee.lengthNorm(fieldName, numTerms);
  }
  
  public float queryNorm(float sumOfSquaredWeights) {
    return delegee.queryNorm(sumOfSquaredWeights);
  }

  public float tf(float freq) {
    return delegee.tf(freq);
  }
    
  public float sloppyFreq(int distance) {
    return delegee.sloppyFreq(distance);
  }
    
  public float idf(int docFreq, int numDocs) {
    return delegee.idf(docFreq, numDocs);
  }
    
  public float coord(int overlap, int maxOverlap) {
    return delegee.coord(overlap, maxOverlap);
  }

  public float scorePayload(String fieldName, byte[] payload, int offset, int length) {
    return delegee.scorePayload(fieldName, payload, offset, length);
  }
}
