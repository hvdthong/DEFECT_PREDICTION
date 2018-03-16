import java.io.IOException;

import org.apache.lucene.index.TermDocs;

/** Expert: A <code>Scorer</code> for documents matching a <code>Term</code>.
 */
final class TermScorer extends Scorer {
  private Weight weight;
  private TermDocs termDocs;
  private byte[] norms;
  private float weightValue;
  private int doc;

  private int pointer;
  private int pointerMax;

  private static final int SCORE_CACHE_SIZE = 32;
  private float[] scoreCache = new float[SCORE_CACHE_SIZE];

  /** Construct a <code>TermScorer</code>.
   * @param weight The weight of the <code>Term</code> in the query.
   * @param td An iterator over the documents matching the <code>Term</code>.
   * @param similarity The </code>Similarity</code> implementation to be used for score computations.
   * @param norms The field norms of the document fields for the <code>Term</code>.
   */
  TermScorer(Weight weight, TermDocs td, Similarity similarity,
             byte[] norms) {
    super(similarity);
    this.weight = weight;
    this.termDocs = td;
    this.norms = norms;
    this.weightValue = weight.getValue();

    for (int i = 0; i < SCORE_CACHE_SIZE; i++)
      scoreCache[i] = getSimilarity().tf(i) * weightValue;
  }

  public void score(HitCollector hc) throws IOException {
    next();
    score(hc, Integer.MAX_VALUE);
  }

  protected boolean score(HitCollector c, int end) throws IOException {
    float[] normDecoder = Similarity.getNormDecoder();
      int f = freqs[pointer];



      if (++pointer >= pointerMax) {
        if (pointerMax != 0) {
          pointer = 0;
        } else {
          return false;
        }
      } 
      doc = docs[pointer];
    }
    return true;
  }

  /** Returns the current document number matching the query.
   * Initially invalid, until {@link #next()} is called the first time.
   */
  public int doc() { return doc; }

  /** Advances to the next document matching the query.
   * <br>The iterator over the matching documents is buffered using
   * {@link TermDocs#read(int[],int[])}.
   * @return true iff there is another document matching the query.
   */
  public boolean next() throws IOException {
    pointer++;
    if (pointer >= pointerMax) {
      if (pointerMax != 0) {
        pointer = 0;
      } else {
        return false;
      }
    } 
    doc = docs[pointer];
    return true;
  }

  public float score() {
    int f = freqs[pointer];

  }

  /** Skips to the first match beyond the current whose document number is
   * greater than or equal to a given target. 
   * <br>The implementation uses {@link TermDocs#skipTo(int)}.
   * @param target The target document number.
   * @return true iff there is such a match.
   */
  public boolean skipTo(int target) throws IOException {
    for (pointer++; pointer < pointerMax; pointer++) {
      if (docs[pointer] >= target) {
        doc = docs[pointer];
        return true;
      }
    }

    boolean result = termDocs.skipTo(target);
    if (result) {
      pointerMax = 1;
      pointer = 0;
      docs[pointer] = doc = termDocs.doc();
      freqs[pointer] = termDocs.freq();
    } else {
      doc = Integer.MAX_VALUE;
    }
    return result;
  }

  /** Returns an explanation of the score for a document.
   * <br>When this method is used, the {@link #next()} method
   * and the {@link #score(HitCollector)} method should not be used.
   * @param doc The document number for the explanation.
   * @todo Modify to make use of {@link TermDocs#skipTo(int)}.
   */
  public Explanation explain(int doc) throws IOException {
    TermQuery query = (TermQuery)weight.getQuery();
    Explanation tfExplanation = new Explanation();
    int tf = 0;
    while (pointer < pointerMax) {
      if (docs[pointer] == doc)
        tf = freqs[pointer];
      pointer++;
    }
    if (tf == 0) {
      while (termDocs.next()) {
        if (termDocs.doc() == doc) {
          tf = termDocs.freq();
        }
      }
    }
    termDocs.close();
    tfExplanation.setValue(getSimilarity().tf(tf));
    tfExplanation.setDescription("tf(termFreq("+query.getTerm()+")="+tf+")");
    
    return tfExplanation;
  }

  /** Returns a string representation of this <code>TermScorer</code>. */
  public String toString() { return "scorer(" + weight + ")"; }
}
