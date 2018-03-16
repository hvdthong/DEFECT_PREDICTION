import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.PriorityQueue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

class NearSpansUnordered implements PayloadSpans {
  private SpanNearQuery query;






  private class CellQueue extends PriorityQueue {
    public CellQueue(int size) {
      initialize(size);
    }
    
    protected final boolean lessThan(Object o1, Object o2) {
      SpansCell spans1 = (SpansCell)o1;
      SpansCell spans2 = (SpansCell)o2;
      if (spans1.doc() == spans2.doc()) {
        return NearSpansOrdered.docSpansOrdered(spans1, spans2);
      } else {
        return spans1.doc() < spans2.doc();
      }
    }
  }


  /** Wraps a Spans, and can be used to form a linked list. */
  private class SpansCell implements PayloadSpans {
    private PayloadSpans spans;
    private SpansCell next;
    private int length = -1;
    private int index;

    public SpansCell(PayloadSpans spans, int index) {
      this.spans = spans;
      this.index = index;
    }

    public boolean next() throws IOException {
      return adjust(spans.next());
    }

    public boolean skipTo(int target) throws IOException {
      return adjust(spans.skipTo(target));
    }
    
    private boolean adjust(boolean condition) {
      if (length != -1) {
      }
      if (condition) {
        length = end() - start(); 

        if (max == null || doc() > max.doc()
            || (doc() == max.doc()) && (end() > max.end())) {
          max = this;
        }
      }
      more = condition;
      return condition;
    }

    public int doc() { return spans.doc(); }
    public int start() { return spans.start(); }
    public int end() { return spans.end(); }
    public Collection/*<byte[]>*/ getPayload() throws IOException {
      return new ArrayList(spans.getPayload());
    }

   public boolean isPayloadAvailable() {
      return spans.isPayloadAvailable();
    }

    public String toString() { return spans.toString() + "#" + index; }
  }


  public NearSpansUnordered(SpanNearQuery query, IndexReader reader)
    throws IOException {
    this.query = query;
    this.slop = query.getSlop();

    SpanQuery[] clauses = query.getClauses();
    queue = new CellQueue(clauses.length);
    for (int i = 0; i < clauses.length; i++) {
      SpansCell cell =
        new SpansCell(clauses[i].getPayloadSpans(reader), i);
      ordered.add(cell);
    }
  }

  public boolean next() throws IOException {
    if (firstTime) {
      initList(true);
      firstTime = false;
    } else if (more) {
      } else {
        more = false;
      }
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

      if (atMatch()) {
        return true;
      }
      
      more = min().next();
      if (more) {
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
        if (min().skipTo(target)) {
          queue.adjustTop();
        } else {
          more = false;
        }
      }
    }
    return more && (atMatch() ||  next());
  }

  private SpansCell min() { return (SpansCell)queue.top(); }

  public int doc() { return min().doc(); }
  public int start() { return min().start(); }
  public int end() { return max.end(); }

  /**
   * WARNING: The List is not necessarily in order of the the positions
   * @return
   * @throws IOException
   */
  public Collection/*<byte[]>*/ getPayload() throws IOException {
    Set/*<byte[]*/ matchPayload = new HashSet();
    for (SpansCell cell = first; cell != null; cell = cell.next) {
      if (cell.isPayloadAvailable()) {
        matchPayload.addAll(cell.getPayload());
      }
    }
    return matchPayload;
  }

  public boolean isPayloadAvailable() {
    SpansCell pointer = min();
    while (pointer != null) {
      if (pointer.isPayloadAvailable()) {
        return true;
      }
      pointer = pointer.next;
    }

    return false;
  }

  public String toString() {
    return getClass().getName() + "("+query.toString()+")@"+
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

  private void addToList(SpansCell cell) throws IOException {
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

  private void queueToList() throws IOException {
    last = first = null;
    while (queue.top() != null) {
      addToList((SpansCell)queue.pop());
    }
  }
  
  private void listToQueue() {
    for (SpansCell cell = first; cell != null; cell = cell.next) {
    }
  }

  private boolean atMatch() {
    return (min().doc() == max.doc())
        && ((max.end() - min().start() - totalLength) <= slop);
  }
}
