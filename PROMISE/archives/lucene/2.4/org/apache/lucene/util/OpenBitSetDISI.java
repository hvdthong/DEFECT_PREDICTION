import java.io.IOException;
import org.apache.lucene.search.DocIdSetIterator;
 
public class OpenBitSetDISI extends OpenBitSet {

  /** Construct an OpenBitSetDISI with its bits set
   * from the doc ids of the given DocIdSetIterator.
   * Also give a maximum size one larger than the largest doc id for which a
   * bit may ever be set on this OpenBitSetDISI.
   */
  public OpenBitSetDISI(DocIdSetIterator disi, int maxSize) throws IOException {
    super(maxSize);
    inPlaceOr(disi);
  }

  /** Construct an OpenBitSetDISI with no bits set, and a given maximum size
   * one larger than the largest doc id for which a bit may ever be set
   * on this OpenBitSetDISI.
   */
  public OpenBitSetDISI(int maxSize) {
    super(maxSize);
  }

  /**
   * Perform an inplace OR with the doc ids from a given DocIdSetIterator,
   * setting the bit for each such doc id.
   * These doc ids should be smaller than the maximum size passed to the
   * constructor.
   */   
  public void inPlaceOr(DocIdSetIterator disi) throws IOException {
    while (disi.next() && (disi.doc() < size())) {
      fastSet(disi.doc());
    }
  }

  /**
   * Perform an inplace AND with the doc ids from a given DocIdSetIterator,
   * leaving only the bits set for which the doc ids are in common.
   * These doc ids should be smaller than the maximum size passed to the
   * constructor.
   */   
  public void inPlaceAnd(DocIdSetIterator disi) throws IOException {
    int index = nextSetBit(0);
    int lastNotCleared = -1;
    while ((index != -1) && disi.skipTo(index)) {
      while ((index != -1) && (index < disi.doc())) {
        fastClear(index);
        index = nextSetBit(index + 1);
      }
      if (index == disi.doc()) {
        lastNotCleared = index;
        index++;
      }
      assert (index == -1) || (index > disi.doc());
    }
    clear(lastNotCleared+1, size());
  }

  /**
   * Perform an inplace NOT with the doc ids from a given DocIdSetIterator,
   * clearing all the bits for each such doc id.
   * These doc ids should be smaller than the maximum size passed to the
   * constructor.
   */   
  public void inPlaceNot(DocIdSetIterator disi) throws IOException {
    while (disi.next() && (disi.doc() < size())) {
      fastClear(disi.doc());
    }
  }

  /**
   * Perform an inplace XOR with the doc ids from a given DocIdSetIterator,
   * flipping all the bits for each such doc id.
   * These doc ids should be smaller than the maximum size passed to the
   * constructor.
   */   
  public void inPlaceXor(DocIdSetIterator disi) throws IOException {
    while (disi.next() && (disi.doc() < size())) {
      fastFlip(disi.doc());
    }
  }
}
