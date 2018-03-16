import org.apache.lucene.util.PriorityQueue;

final class HitQueue extends PriorityQueue {
  HitQueue(int size) {
    initialize(size);
  }

  protected final boolean lessThan(Object a, Object b) {
    ScoreDoc hitA = (ScoreDoc)a;
    ScoreDoc hitB = (ScoreDoc)b;
    if (hitA.score == hitB.score)
      return hitA.doc > hitB.doc; 
    else
      return hitA.score < hitB.score;
  }
}
