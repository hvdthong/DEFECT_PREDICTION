import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/** Scorer for conjunctions, sets of queries, all of which are required. */
class ConjunctionScorer extends Scorer {
  private LinkedList scorers = new LinkedList();
  private boolean firstTime = true;
  private boolean more = true;
  private float coord;

  public ConjunctionScorer(Similarity similarity) {
    super(similarity);
  }

  final void add(Scorer scorer) {
    scorers.addLast(scorer);
  }

  private Scorer first() { return (Scorer)scorers.getFirst(); }
  private Scorer last() { return (Scorer)scorers.getLast(); }

  public int doc() { return first().doc(); }

  public boolean next() throws IOException {
    if (firstTime) {
      init(true);
    } else if (more) {
    }
    return doNext();
  }
  
  private boolean doNext() throws IOException {
    }
  }

  public boolean skipTo(int target) throws IOException {
    if(firstTime) {
      init(false);
    }
    
    Iterator i = scorers.iterator();
    while (more && i.hasNext()) {
      more = ((Scorer)i.next()).skipTo(target);
    }
    
    if (more)
    
    return doNext();
  }

  public float score() throws IOException {
    Iterator i = scorers.iterator();
    while (i.hasNext())
      score += ((Scorer)i.next()).score();
    score *= coord;
    return score;
  }
  
  private void init(boolean initScorers) throws IOException {
    coord = getSimilarity().coord(scorers.size(), scorers.size());
   
    more = scorers.size() > 0;

    if(initScorers){
      Iterator i = scorers.iterator();
      while (more && i.hasNext()) {
        more = ((Scorer)i.next()).next();
      }
      if (more)
    }

    firstTime = false;
  }

  private void sortScorers() {
    Scorer[] array = (Scorer[])scorers.toArray(new Scorer[scorers.size()]);

        public int compare(Object o1, Object o2) {
          return ((Scorer)o1).doc() - ((Scorer)o2).doc();
        }
      });
    
    for (int i = 0; i < array.length; i++) {
    }
  }

  public Explanation explain(int doc) {
    throw new UnsupportedOperationException();
  }

}
