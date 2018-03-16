 
import java.io.IOException;

/** A scorer that matches no document at all. */
class NonMatchingScorer extends Scorer {
  
  public int doc() { throw new UnsupportedOperationException(); }

  public boolean next() throws IOException { return false; }

  public float score() { throw new UnsupportedOperationException(); }

  public boolean skipTo(int target) { return false; }

  public Explanation explain(int doc) {
    Explanation e = new Explanation();
    e.setDescription("No document matches.");
    return e;
  }
}
 

