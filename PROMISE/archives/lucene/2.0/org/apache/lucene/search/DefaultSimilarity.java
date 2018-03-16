public class DefaultSimilarity extends Similarity {
  /** Implemented as <code>1/sqrt(numTerms)</code>. */
  public float lengthNorm(String fieldName, int numTerms) {
    return (float)(1.0 / Math.sqrt(numTerms));
  }
  
  /** Implemented as <code>1/sqrt(sumOfSquaredWeights)</code>. */
  public float queryNorm(float sumOfSquaredWeights) {
    return (float)(1.0 / Math.sqrt(sumOfSquaredWeights));
  }

  /** Implemented as <code>sqrt(freq)</code>. */
  public float tf(float freq) {
    return (float)Math.sqrt(freq);
  }
    
  /** Implemented as <code>1 / (distance + 1)</code>. */
  public float sloppyFreq(int distance) {
    return 1.0f / (distance + 1);
  }
    
  /** Implemented as <code>log(numDocs/(docFreq+1)) + 1</code>. */
  public float idf(int docFreq, int numDocs) {
    return (float)(Math.log(numDocs/(double)(docFreq+1)) + 1.0);
  }
    
  /** Implemented as <code>overlap / maxOverlap</code>. */
  public float coord(int overlap, int maxOverlap) {
    return overlap / (float)maxOverlap;
  }
}
