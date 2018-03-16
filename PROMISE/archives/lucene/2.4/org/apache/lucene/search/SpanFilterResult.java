import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


/**
 *  The results of a SpanQueryFilter.  Wraps the BitSet and the position infomration from the SpanQuery
 *
 *<p/>
 * NOTE: This API is still experimental and subject to change. 
 *
 **/
public class SpanFilterResult {
  /** @deprecated */
  private BitSet bits;
  
  private DocIdSet docIdSet;

  /**
   *
   * @param bits The bits for the Filter
   * @param positions A List of {@link org.apache.lucene.search.SpanFilterResult.PositionInfo} objects
   * @deprecated Use {@link #SpanFilterResult(DocIdSet, List)} instead
   */
  public SpanFilterResult(BitSet bits, List positions) {
    this.bits = bits;
    this.positions = positions;
  }
  
  /**
  *
  * @param docIdSet The DocIdSet for the Filter
  * @param positions A List of {@link org.apache.lucene.search.SpanFilterResult.PositionInfo} objects
  */
  public SpanFilterResult(DocIdSet docIdSet, List positions) {
    this.docIdSet = docIdSet;
    this.positions = positions;
  }
  
  /**
   * The first entry in the array corresponds to the first "on" bit.
   * Entries are increasing by document order
   * @return A List of PositionInfo objects
   */
  public List getPositions() {
    return positions;
  }

  /** 
   * @deprecated Use {@link #getDocIdSet()}
   */
  public BitSet getBits() {
    return bits;
  }
  
  /** Returns the docIdSet */
  public DocIdSet getDocIdSet() {
    return docIdSet;
  }

  public static class PositionInfo {
    private int doc;
    private List positions;


    public PositionInfo(int doc) {
      this.doc = doc;
      positions = new ArrayList();
    }

    public void addPosition(int start, int end)
    {
      positions.add(new StartEnd(start, end));
    }

    public int getDoc() {
      return doc;
    }

    /**
     *
     * @return A List of {@link org.apache.lucene.search.SpanFilterResult.StartEnd} objects
     */
    public List getPositions() {
      return positions;
    }
  }

  public static class StartEnd
  {
    private int start;
    private int end;


    public StartEnd(int start, int end) {
      this.start = start;
      this.end = end;
    }

    /**
     *
     * @return The end position of this match
     */
    public int getEnd() {
      return end;
    }

    /**
     * The Start position
     * @return The start position of this match
     */
    public int getStart() {
      return start;
    }

  }
}



