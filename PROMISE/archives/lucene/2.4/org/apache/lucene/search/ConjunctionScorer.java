import java.io.IOException;
import java.util.Collection;
import java.util.Arrays;
import java.util.Comparator;

/** Scorer for conjunctions, sets of queries, all of which are required. */
class ConjunctionScorer extends Scorer {
  private final Scorer[] scorers;

  private boolean firstTime=true;
  private boolean more;
  private final float coord;
  private int lastDoc=-1;

  public ConjunctionScorer(Similarity similarity, Collection scorers) throws IOException {
    this(similarity, (Scorer[])scorers.toArray(new Scorer[scorers.size()]));
  }

  public ConjunctionScorer(Similarity similarity, Scorer[] scorers) throws IOException {
    super(similarity);
    this.scorers = scorers;
    coord = getSimilarity().coord(this.scorers.length, this.scorers.length);
  }

  public int doc() { return lastDoc; }

  public boolean next() throws IOException {
    if (firstTime)
      return init(0);
    else if (more)
      more = scorers[(scorers.length-1)].next();
    return doNext();
  }

  private boolean doNext() throws IOException {
    int first=0;
    Scorer lastScorer = scorers[scorers.length-1];
    Scorer firstScorer;
    while (more && (firstScorer=scorers[first]).doc() < (lastDoc=lastScorer.doc())) {
      more = firstScorer.skipTo(lastDoc);
      lastScorer = firstScorer;
      first = (first == (scorers.length-1)) ? 0 : first+1;
    }
    return more;
  }

  public boolean skipTo(int target) throws IOException {
    if (firstTime)
      return init(target);
    else if (more)
      more = scorers[(scorers.length-1)].skipTo(target);
    return doNext();
  }

  private boolean init(int target) throws IOException {
    firstTime=false;
    more = scorers.length>1;
    for (int i=0; i<scorers.length; i++) {
      more = target==0 ? scorers[i].next() : scorers[i].skipTo(target);
      if (!more)
        return false;
    }


        public int compare(Object o1, Object o2) {
          return ((Scorer)o1).doc() - ((Scorer)o2).doc();
        }
      });

    doNext();

    int end=(scorers.length-1);
    for (int i=0; i<(end>>1); i++) {
      Scorer tmp = scorers[i];
      scorers[i] = scorers[end-i-1];
      scorers[end-i-1] = tmp;
    }

    return more;
  }

  public float score() throws IOException {
    float sum = 0.0f;
    for (int i = 0; i < scorers.length; i++) {
      sum += scorers[i].score();
    }
    return sum * coord;
  }

  public Explanation explain(int doc) {
    throw new UnsupportedOperationException();
  }

}
