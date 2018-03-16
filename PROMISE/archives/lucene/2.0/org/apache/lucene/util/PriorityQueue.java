  least element can always be found in constant time.  Put()'s and pop()'s
  require log(size) time. */
public abstract class PriorityQueue {
  private Object[] heap;
  private int size;
  private int maxSize;

  /** Determines the ordering of objects in this priority queue.  Subclasses
    must define this one method. */
  protected abstract boolean lessThan(Object a, Object b);

  /** Subclass constructors must call this. */
  protected final void initialize(int maxSize) {
    size = 0;
    int heapSize = maxSize + 1;
    heap = new Object[heapSize];
    this.maxSize = maxSize;
  }

  /**
   * Adds an Object to a PriorityQueue in log(size) time.
   * If one tries to add more objects than maxSize from initialize
   * a RuntimeException (ArrayIndexOutOfBound) is thrown.
   */
  public final void put(Object element) {
    size++;
    heap[size] = element;
    upHeap();
  }

  /**
   * Adds element to the PriorityQueue in log(size) time if either
   * the PriorityQueue is not full, or not lessThan(element, top()).
   * @param element
   * @return true if element is added, false otherwise.
   */
  public boolean insert(Object element){
    if(size < maxSize){
      put(element);
      return true;
    }
    else if(size > 0 && !lessThan(element, top())){
      heap[1] = element;
      adjustTop();
      return true;
    }
    else
      return false;
   }

  /** Returns the least element of the PriorityQueue in constant time. */
  public final Object top() {
    if (size > 0)
      return heap[1];
    else
      return null;
  }

  /** Removes and returns the least element of the PriorityQueue in log(size)
    time. */
  public final Object pop() {
    if (size > 0) {
      size--;
      return result;
    } else
      return null;
  }

  /** Should be called when the Object at top changes values.  Still log(n)
   * worst case, but it's at least twice as fast to <pre>
   *  { pq.top().change(); pq.adjustTop(); }
   * </pre> instead of <pre>
   *  { o = pq.pop(); o.change(); pq.push(o); }
   * </pre>
   */
  public final void adjustTop() {
    downHeap();
  }


  /** Returns the number of elements currently stored in the PriorityQueue. */
  public final int size() {
    return size;
  }

  /** Removes all entries from the PriorityQueue. */
  public final void clear() {
    for (int i = 0; i <= size; i++)
      heap[i] = null;
    size = 0;
  }

  private final void upHeap() {
    int i = size;
    int j = i >>> 1;
    while (j > 0 && lessThan(node, heap[j])) {
      i = j;
      j = j >>> 1;
    }
  }

  private final void downHeap() {
    int i = 1;
    int k = j + 1;
    if (k <= size && lessThan(heap[k], heap[j])) {
      j = k;
    }
    while (j <= size && lessThan(heap[j], node)) {
      i = j;
      j = i << 1;
      k = j + 1;
      if (k <= size && lessThan(heap[k], heap[j])) {
	j = k;
      }
    }
  }
}
