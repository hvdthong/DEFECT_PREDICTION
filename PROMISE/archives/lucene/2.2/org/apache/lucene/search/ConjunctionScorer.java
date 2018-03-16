import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/** Scorer for conjunctions, sets of queries, all of which are required. */
class ConjunctionScorer extends Scorer {
  private Scorer[] scorers = new Scorer[2];
  private int length = 0;
  private int first = 0;
  private int last = -1;
  private boolean firstTime = true;
  private boolean more = true;
  private float coord;

  public ConjunctionScorer(Similarity similarity) {
    super(similarity);
  }

  final void add(Scorer scorer) {
    if (length >= scorers.length) {
      Scorer[] temps = new Scorer[scorers.length * 2];
      System.arraycopy(scorers, 0, temps, 0, length);
      scorers = temps;
    }
    last += 1;
    length += 1;
    scorers[last] = scorer;
  }

  public int doc() { return scorers[first].doc(); }

  public boolean next() throws IOException {
    if (firstTime) {
      init(true);
    } else if (more) {
    }
    return doNext();
  }
  
  private boolean doNext() throws IOException {
      first = (first == length-1) ? 0 : first+1;
    }
  }

  public boolean skipTo(int target) throws IOException {
    if(firstTime) {
      init(false);
    }
    
    for (int i = 0, pos = first; i < length; i++) {
      if (!more) break; 
      more = scorers[pos].skipTo(target);
      pos = (pos == length-1) ? 0 : pos+1;
    }
    
    if (more)
    
    return doNext();
  }

  public float score() throws IOException {
    float sum = 0.0f;
    for (int i = 0; i < length; i++) {
      sum += scorers[i].score();
    }
    return sum * coord;
  }
  
  private void init(boolean initScorers) throws IOException {
    coord = getSimilarity().coord(length, length);
   
    more = length > 0;

    if(initScorers){
      for (int i = 0, pos = first; i < length; i++) {
        if (!more) break; 
        more = scorers[pos].next();
        pos = (pos == length-1) ? 0 : pos+1;
      }
      if (more) 
        sortScorers();
    }

    firstTime = false;
  }

  private void sortScorers() {
    if (length != scorers.length) {
      Scorer[] temps = new Scorer[length];
      System.arraycopy(scorers, 0, temps, 0, length);
      scorers = temps;
    }
    
        public int compare(Object o1, Object o2) {
          return ((Scorer)o1).doc() - ((Scorer)o2).doc();
        }
      });
   
    first = 0;
    last = length - 1;
  }

  public Explanation explain(int doc) {
    throw new UnsupportedOperationException();
  }

}
