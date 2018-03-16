import java.util.BitSet;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;


/** Simple DocIdSet and DocIdSetIterator backed by a BitSet */
public class DocIdBitSet extends DocIdSet {
  private BitSet bitSet;
    
  public DocIdBitSet(BitSet bitSet) {
    this.bitSet = bitSet;
  }

  public DocIdSetIterator iterator() {
    return new DocIdBitSetIterator(bitSet);
  }
  
  /**
   * Returns the underlying BitSet. 
   */
  public BitSet getBitSet() {
	return this.bitSet;
  }
  
  private static class DocIdBitSetIterator extends DocIdSetIterator {
    private int docId;
    private BitSet bitSet;
    
    DocIdBitSetIterator(BitSet bitSet) {
      this.bitSet = bitSet;
      this.docId = -1;
    }
    
    public int doc() {
      assert docId != -1;
      return docId;
    }
    
    public boolean next() {
      return checkNextDocId(bitSet.nextSetBit(docId + 1));
    }
  
    public boolean skipTo(int skipDocNr) {
      return checkNextDocId( bitSet.nextSetBit(skipDocNr));
    }
  
    private boolean checkNextDocId(int d) {
        docId = Integer.MAX_VALUE;
        return false;
      } else {
        docId = d;
        return true;
      }
    }
  }
}
