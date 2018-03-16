class SegmentTermPositionVector extends SegmentTermVector implements TermPositionVector {
  protected int[][] positions;
  protected TermVectorOffsetInfo[][] offsets;
  public static final int[] EMPTY_TERM_POS = new int[0];
  
  public SegmentTermPositionVector(String field, String terms[], int termFreqs[], int[][] positions, TermVectorOffsetInfo[][] offsets) {
    super(field, terms, termFreqs);
    this.offsets = offsets;
    this.positions = positions;
  }

  /**
   * Returns an array of TermVectorOffsetInfo in which the term is found.
   *
   * @param index The position in the array to get the offsets from
   * @return An array of TermVectorOffsetInfo objects or the empty list
   * @see org.apache.lucene.analysis.Token
   */
  public TermVectorOffsetInfo[] getOffsets(int index) {
    TermVectorOffsetInfo[] result = TermVectorOffsetInfo.EMPTY_OFFSET_INFO;
    if(offsets == null)
      return null;
    if (index >=0 && index < offsets.length)
    {
      result = offsets[index];
    }
    return result;
  }
  
  /**
   * Returns an array of positions in which the term is found.
   * Terms are identified by the index at which its number appears in the
   * term String array obtained from the <code>indexOf</code> method.
   */
  public int[] getTermPositions(int index) {
    int[] result = EMPTY_TERM_POS;
    if(positions == null)
      return null;
    if (index >=0 && index < positions.length)
    {
      result = positions[index];
    }
    
    return result;
  }
}
