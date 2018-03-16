import java.io.IOException;

final class BooleanScorer extends Scorer {
  private SubScorer scorers = null;
  private BucketTable bucketTable = new BucketTable(this);

  private int maxCoord = 1;
  private float[] coordFactors = null;

  private int requiredMask = 0;
  private int prohibitedMask = 0;
  private int nextMask = 1;

  BooleanScorer(Similarity similarity) {
    super(similarity);
  }

  static final class SubScorer {
    public Scorer scorer;
    public boolean done;
    public boolean required = false;
    public boolean prohibited = false;
    public HitCollector collector;
    public SubScorer next;

    public SubScorer(Scorer scorer, boolean required, boolean prohibited,
                     HitCollector collector, SubScorer next)
      throws IOException {
      this.scorer = scorer;
      this.done = !scorer.next();
      this.required = required;
      this.prohibited = prohibited;
      this.collector = collector;
      this.next = next;
    }
  }

  final void add(Scorer scorer, boolean required, boolean prohibited)
    throws IOException {
    int mask = 0;
    if (required || prohibited) {
      if (nextMask == 0)
        throw new IndexOutOfBoundsException
          ("More than 32 required/prohibited clauses in query.");
      mask = nextMask;
      nextMask = nextMask << 1;
    } else
      mask = 0;

    if (!prohibited)
      maxCoord++;

    if (prohibited)
    else if (required)

    scorers = new SubScorer(scorer, required, prohibited,
                            bucketTable.newCollector(mask), scorers);
  }

  private final void computeCoordFactors() {
    coordFactors = new float[maxCoord];
    for (int i = 0; i < maxCoord; i++)
      coordFactors[i] = getSimilarity().coord(i, maxCoord-1);
  }

  private int end;
  private Bucket current;

  public void score(HitCollector hc) throws IOException {
    next();
    score(hc, Integer.MAX_VALUE);
  }

  protected boolean score(HitCollector hc, int max) throws IOException {
    if (coordFactors == null)
      computeCoordFactors();

    boolean more;
    Bucket tmp;
    
    do {
      bucketTable.first = null;
      

        if ((current.bits & prohibitedMask) == 0 && 
            (current.bits & requiredMask) == requiredMask) {
          
          if (current.doc >= max){
            tmp = current;
            current = current.next;
            tmp.next = bucketTable.first;
            bucketTable.first = tmp;
            continue;
          }
          
          hc.collect(current.doc, current.score * coordFactors[current.coord]);
        }
        
      }
      
      if( bucketTable.first != null){
        current = bucketTable.first;
        bucketTable.first = current.next;
        return true;
      }

      more = false;
      end += BucketTable.SIZE;
      for (SubScorer sub = scorers; sub != null; sub = sub.next) {
        if (!sub.done) {
          sub.done = !sub.scorer.score(sub.collector, end);
          if (!sub.done)
            more = true;
        }
      }
      current = bucketTable.first;
      
    } while (current != null || more);

    return false;
  }

  public int doc() { return current.doc; }

  public boolean next() throws IOException {
    boolean more;
    do {
        current = bucketTable.first;

        if ((current.bits & prohibitedMask) == 0 && 
            (current.bits & requiredMask) == requiredMask) {
          return true;
        }
      }

      more = false;
      end += BucketTable.SIZE;
      for (SubScorer sub = scorers; sub != null; sub = sub.next) {
        Scorer scorer = sub.scorer;
        while (!sub.done && scorer.doc() < end) {
          sub.collector.collect(scorer.doc(), scorer.score());
          sub.done = !scorer.next();
        }
        if (!sub.done) {
          more = true;
        }
      }
    } while (bucketTable.first != null || more);

    return false;
  }

  public float score() {
    if (coordFactors == null)
      computeCoordFactors();
    return current.score * coordFactors[current.coord];
  }

  static final class Bucket {
  }

  /** A simple hash table of document scores within a range. */
  static final class BucketTable {
    public static final int SIZE = 1 << 11;
    public static final int MASK = SIZE - 1;

    final Bucket[] buckets = new Bucket[SIZE];
  
    private BooleanScorer scorer;

    public BucketTable(BooleanScorer scorer) {
      this.scorer = scorer;
    }

    public final int size() { return SIZE; }

    public HitCollector newCollector(int mask) {
      return new Collector(mask, this);
    }
  }

  static final class Collector extends HitCollector {
    private BucketTable bucketTable;
    private int mask;
    public Collector(int mask, BucketTable bucketTable) {
      this.mask = mask;
      this.bucketTable = bucketTable;
    }
    public final void collect(final int doc, final float score) {
      final BucketTable table = bucketTable;
      final int i = doc & BucketTable.MASK;
      Bucket bucket = table.buckets[i];
      if (bucket == null)
        table.buckets[i] = bucket = new Bucket();
      

        table.first = bucket;
      }
    }
  }

  public boolean skipTo(int target) {
    throw new UnsupportedOperationException();
  }

  public Explanation explain(int doc) {
    throw new UnsupportedOperationException();
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("boolean(");
    for (SubScorer sub = scorers; sub != null; sub = sub.next) {
      buffer.append(sub.scorer.toString());
      buffer.append(" ");
    }
    buffer.append(")");
    return buffer.toString();
  }


}
