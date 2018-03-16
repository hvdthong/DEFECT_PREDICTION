import java.io.IOException;
import org.apache.lucene.search.Scorer;

/** A ScorerDocQueue maintains a partial ordering of its Scorers such that the
  least Scorer can always be found in constant time.  Put()'s and pop()'s
  require log(size) time. The ordering is by Scorer.doc().
 */
  private final HeapedScorerDoc[] heap;
  private final int maxSize;
  private int size;
  
  private class HeapedScorerDoc {
    Scorer scorer;
    int doc;
    
    HeapedScorerDoc(Scorer s) { this(s, s.doc()); }
    
    HeapedScorerDoc(Scorer scorer, int doc) {
      this.scorer = scorer;
      this.doc = doc;
    }
    
    void adjust() { doc = scorer.doc(); }
  }
  

  /** Create a ScorerDocQueue with a maximum size. */
  public ScorerDocQueue(int maxSize) {
    size = 0;
    int heapSize = maxSize + 1;
    heap = new HeapedScorerDoc[heapSize];
    this.maxSize = maxSize;
  }

  /**
   * Adds a Scorer to a ScorerDocQueue in log(size) time.
   * If one tries to add more Scorers than maxSize
   * a RuntimeException (ArrayIndexOutOfBound) is thrown.
   */
  public final void put(Scorer scorer) {
    size++;
    heap[size] = new HeapedScorerDoc(scorer);
    upHeap();
  }

  /**
   * Adds a Scorer to the ScorerDocQueue in log(size) time if either
   * the ScorerDocQueue is not full, or not lessThan(scorer, top()).
   * @param scorer
   * @return true if scorer is added, false otherwise.
   */
  public boolean insert(Scorer scorer){
    if (size < maxSize) {
      put(scorer);
      return true;
    } else {
      int docNr = scorer.doc();
        heap[1] = new HeapedScorerDoc(scorer, docNr);
        downHeap();
        return true;
      } else {
        return false;
      }
    }
   }

  /** Returns the least Scorer of the ScorerDocQueue in constant time.
   * Should not be used when the queue is empty.
   */
  public final Scorer top() {
    return topHSD.scorer;
  }

  /** Returns document number of the least Scorer of the ScorerDocQueue
   * in constant time.
   * Should not be used when the queue is empty.
   */
  public final int topDoc() {
    return topHSD.doc;
  }
  
  public final float topScore() throws IOException {
    return topHSD.scorer.score();
  }

  public final boolean topNextAndAdjustElsePop() throws IOException {
    return checkAdjustElsePop( topHSD.scorer.next());
  }

  public final boolean topSkipToAndAdjustElsePop(int target) throws IOException {
    return checkAdjustElsePop( topHSD.scorer.skipTo(target));
  }
  
  private boolean checkAdjustElsePop(boolean cond) {
      topHSD.doc = topHSD.scorer.doc();
      heap[size] = null;
      size--;
    }
    downHeap();
    return cond;
  }

  /** Removes and returns the least scorer of the ScorerDocQueue in log(size)
   * time.
   * Should not be used when the queue is empty.
   */
  public final Scorer pop() {
    Scorer result = topHSD.scorer;
    popNoResult();
    return result;
  }
  
  /** Removes the least scorer of the ScorerDocQueue in log(size) time.
   * Should not be used when the queue is empty.
   */
  private final void popNoResult() {
    heap[size] = null;
    size--;
  }

  /** Should be called when the scorer at top changes doc() value.
   * Still log(n) worst case, but it's at least twice as fast to <pre>
   *  { pq.top().change(); pq.adjustTop(); }
   * </pre> instead of <pre>
   *  { o = pq.pop(); o.change(); pq.push(o); }
   * </pre>
   */
  public final void adjustTop() {
    topHSD.adjust();
    downHeap();
  }

  /** Returns the number of scorers currently stored in the ScorerDocQueue. */
  public final int size() {
    return size;
  }

  /** Removes all entries from the ScorerDocQueue. */
  public final void clear() {
    for (int i = 0; i <= size; i++) {
      heap[i] = null;
    }
    size = 0;
  }

  private final void upHeap() {
    int i = size;
    int j = i >>> 1;
    while ((j > 0) && (node.doc < heap[j].doc)) {
      i = j;
      j = j >>> 1;
    }
    topHSD = heap[1];
  }

  private final void downHeap() {
    int i = 1;
    int k = j + 1;
    if ((k <= size) && (heap[k].doc < heap[j].doc)) {
      j = k;
    }
    while ((j <= size) && (heap[j].doc < node.doc)) {
      i = j;
      j = i << 1;
      k = j + 1;
      if (k <= size && (heap[k].doc < heap[j].doc)) {
	j = k;
      }
    }
    topHSD = heap[1];
  }
}
