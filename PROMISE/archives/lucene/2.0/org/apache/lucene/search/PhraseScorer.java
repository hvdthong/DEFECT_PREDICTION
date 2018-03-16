import java.io.IOException;

import org.apache.lucene.index.*;

abstract class PhraseScorer extends Scorer {
  private Weight weight;
  protected byte[] norms;
  protected float value;

  private boolean firstTime = true;
  private boolean more = true;
  protected PhraseQueue pq;
  protected PhrasePositions first, last;

  private float freq;


  PhraseScorer(Weight weight, TermPositions[] tps, int[] positions, Similarity similarity,
               byte[] norms) {
    super(similarity);
    this.norms = norms;
    this.weight = weight;
    this.value = weight.getValue();

    for (int i = 0; i < tps.length; i++) {
      PhrasePositions pp = new PhrasePositions(tps[i], positions[i]);
        last.next = pp;
      } else
        first = pp;
      last = pp;
    }


  }

  public int doc() { return first.doc; }

  public boolean next() throws IOException {
    if (firstTime) {
      init();
      firstTime = false;
    } else if (more) {
    }
    return doNext();
  }
  
  private boolean doNext() throws IOException {
    while (more) {
      }

      if (more) {
        else
      }
    }
  }

  public float score() throws IOException {
  }

  public boolean skipTo(int target) throws IOException {
    for (PhrasePositions pp = first; more && pp != null; pp = pp.next) {
      more = pp.skipTo(target);
    }
    if (more)
    return doNext();
  }

  protected abstract float phraseFreq() throws IOException;

  private void init() throws IOException {
    for (PhrasePositions pp = first; more && pp != null; pp = pp.next) 
      more = pp.next();
    if(more)
      sort();
  }
  
  private void sort() {
    pq.clear();
    for (PhrasePositions pp = first; pp != null; pp = pp.next)
      pq.put(pp);
    pqToList();
  }

  protected final void pqToList() {
    last = first = null;
    while (pq.top() != null) {
      PhrasePositions pp = (PhrasePositions) pq.pop();
        last.next = pp;
      } else
        first = pp;
      last = pp;
      pp.next = null;
    }
  }

  protected final void firstToLast() {
    last = first;
    first = first.next;
    last.next = null;
  }

  public Explanation explain(final int doc) throws IOException {
    Explanation tfExplanation = new Explanation();

    while (next() && doc() < doc) {}

    float phraseFreq = (doc() == doc) ? freq : 0.0f;
    tfExplanation.setValue(getSimilarity().tf(phraseFreq));
    tfExplanation.setDescription("tf(phraseFreq=" + phraseFreq + ")");

    return tfExplanation;
  }

  public String toString() { return "scorer(" + weight + ")"; }

}
