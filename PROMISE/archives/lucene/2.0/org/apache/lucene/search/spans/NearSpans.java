import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.PriorityQueue;

class NearSpans implements Spans {
  private SpanNearQuery query;






  private class CellQueue extends PriorityQueue {
    public CellQueue(int size) {
      initialize(size);
    }
    
    protected final boolean lessThan(Object o1, Object o2) {
      SpansCell spans1 = (SpansCell)o1;
      SpansCell spans2 = (SpansCell)o2;
      if (spans1.doc() == spans2.doc()) {
        if (spans1.start() == spans2.start()) {
          if (spans1.end() == spans2.end()) {
            return spans1.index > spans2.index;
          } else {
            return spans1.end() < spans2.end();
          }
        } else {
          return spans1.start() < spans2.start();
        }
      } else {
        return spans1.doc() < spans2.doc();
      }
    }
  }


  /** Wraps a Spans, and can be used to form a linked list. */
  private class SpansCell implements Spans {
    private Spans spans;
    private SpansCell next;
    private int length = -1;
    private int index;

    public SpansCell(Spans spans, int index) {
      this.spans = spans;
      this.index = index;
    }

    public boolean next() throws IOException {
        totalLength -= length;


      if (more) {

            (doc() == max.doc() && end() > max.end()))
          max = this;
      }

      return more;
    }

    public boolean skipTo(int target) throws IOException {
        totalLength -= length;


      if (more) {

            (doc() == max.doc() && end() > max.end()))
          max = this;
      }

      return more;
    }

    public int doc() { return spans.doc(); }
    public int start() { return spans.start(); }
    public int end() { return spans.end(); }

    public String toString() { return spans.toString() + "#" + index; }
  }

  public NearSpans(SpanNearQuery query, IndexReader reader)
    throws IOException {
    this.query = query;
    this.slop = query.getSlop();
    this.inOrder = query.isInOrder();

    queue = new CellQueue(clauses.length);
    for (int i = 0; i < clauses.length; i++) {
        new SpansCell(clauses[i].getSpans(reader), i);
    }
  }

  public boolean next() throws IOException {
    if (firstTime) {
      initList(true);
      firstTime = false;
    } else if (more) {
      if (more)
    }

    while (more) {

      boolean queueStale = false;

        queueToList();
        queueStale = true;
      }


      while (more && first.doc() < last.doc()) {
        queueStale = true;
      }

      if (!more) return false;


        listToQueue();
        queueStale = false;
      }

      if (atMatch())
        return true;
      
      if (inOrder && checkSlop()) {
        /* There is a non ordered match within slop and an ordered match is needed. */
        more = firstNonOrderedNextToPartialList();
        if (more) {
          partialListToQueue();                            
        }
      } else {
        more = min().next();
        if (more) {
        }
      }
    }
  }

  public boolean skipTo(int target) throws IOException {
      initList(false);
      for (SpansCell cell = first; more && cell!=null; cell=cell.next) {
      }
      if (more) {
        listToQueue();
      }
      firstTime = false;
        more = min().skipTo(target);
        if (more)
          queue.adjustTop();
      }
    }
    if (more) {

        return true;

    }

    return false;
  }

  private SpansCell min() { return (SpansCell)queue.top(); }

  public int doc() { return min().doc(); }
  public int start() { return min().start(); }
  public int end() { return max.end(); }


  public String toString() {
    return "spans("+query.toString()+")@"+
      (firstTime?"START":(more?(doc()+":"+start()+"-"+end()):"END"));
  }

  private void initList(boolean next) throws IOException {
    for (int i = 0; more && i < ordered.size(); i++) {
      SpansCell cell = (SpansCell)ordered.get(i);
      if (next)
      if (more) {
      }
    }
  }

  private void addToList(SpansCell cell) {
      last.next = cell;
    } else
      first = cell;
    last = cell;
    cell.next = null;
  }

  private void firstToLast() {
    last = first;
    first = first.next;
    last.next = null;
  }

  private void queueToList() {
    last = first = null;
    while (queue.top() != null) {
      addToList((SpansCell)queue.pop());
    }
  }
  
  private boolean firstNonOrderedNextToPartialList() throws IOException {
    /* Creates a partial list consisting of first non ordered and earlier.
     * Returns first non ordered .next().
     */
    last = first = null;
    int orderedIndex = 0;
    while (queue.top() != null) {
      SpansCell cell = (SpansCell)queue.pop();
      addToList(cell);
      if (cell.index == orderedIndex) {
        orderedIndex++;
      } else {
        return cell.next();
      }
    }
    throw new RuntimeException("Unexpected: ordered");
  }

  private void listToQueue() {
    partialListToQueue();
  }

  private void partialListToQueue() {
    for (SpansCell cell = first; cell != null; cell = cell.next) {
    }
  }

  private boolean atMatch() {
    return (min().doc() == max.doc())
          && checkSlop()
          && (!inOrder || matchIsOrdered());
  }
  
  private boolean checkSlop() {
    int matchLength = max.end() - min().start();
    return (matchLength - totalLength) <= slop;
  }

  private boolean matchIsOrdered() {
    int lastStart = -1;
    for (int i = 0; i < ordered.size(); i++) {
      int start = ((SpansCell)ordered.get(i)).start();
      if (!(start > lastStart))
        return false;
      lastStart = start;
    }
    return true;
  }
}
