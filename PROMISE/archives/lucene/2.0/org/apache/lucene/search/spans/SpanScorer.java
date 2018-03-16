import java.io.IOException;

import org.apache.lucene.search.Weight;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Similarity;


class SpanScorer extends Scorer {
  private Spans spans;
  private Weight weight;
  private byte[] norms;
  private float value;

  private boolean firstTime = true;
  private boolean more = true;

  private int doc;
  private float freq;

  SpanScorer(Spans spans, Weight weight, Similarity similarity, byte[] norms)
    throws IOException {
    super(similarity);
    this.spans = spans;
    this.norms = norms;
    this.weight = weight;
    this.value = weight.getValue();
    doc = -1;
  }

  public boolean next() throws IOException {
    if (firstTime) {
      more = spans.next();
      firstTime = false;
    }
    return setFreqCurrentDoc();
  }

  public boolean skipTo(int target) throws IOException {
    if (firstTime) {
      more = spans.skipTo(target);
      firstTime = false;
    }
    if (! more) {
      return false;
    }
      more = spans.skipTo(target);
    }
    return setFreqCurrentDoc();
  }

  private boolean setFreqCurrentDoc() throws IOException {
    if (! more) {
      return false;
    }
    doc = spans.doc();
    freq = 0.0f;
    while (more && doc == spans.doc()) {
      int matchLength = spans.end() - spans.start();
      freq += getSimilarity().sloppyFreq(matchLength);
      more = spans.next();
    }
    return more || (freq != 0);
  }

  public int doc() { return doc; }

  public float score() throws IOException {
  }

  public Explanation explain(final int doc) throws IOException {
    Explanation tfExplanation = new Explanation();

    skipTo(doc);

    float phraseFreq = (doc() == doc) ? freq : 0.0f;
    tfExplanation.setValue(getSimilarity().tf(phraseFreq));
    tfExplanation.setDescription("tf(phraseFreq=" + phraseFreq + ")");

    return tfExplanation;
  }

}
