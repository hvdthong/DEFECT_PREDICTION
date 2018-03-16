import java.io.IOException;
import org.apache.lucene.util.PriorityQueue;

final class SegmentMergeQueue extends PriorityQueue {
  SegmentMergeQueue(int size) {
    initialize(size);
  }

  protected final boolean lessThan(Object a, Object b) {
    SegmentMergeInfo stiA = (SegmentMergeInfo)a;
    SegmentMergeInfo stiB = (SegmentMergeInfo)b;
    int comparison = stiA.term.compareTo(stiB.term);
    if (comparison == 0)
      return stiA.base < stiB.base; 
    else
      return comparison < 0;
  }

  final void close() throws IOException {
    while (top() != null)
      ((SegmentMergeInfo)pop()).close();
  }

}
