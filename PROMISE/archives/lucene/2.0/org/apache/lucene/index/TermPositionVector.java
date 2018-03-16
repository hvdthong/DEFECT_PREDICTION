public interface TermPositionVector extends TermFreqVector {
  
    /** Returns an array of positions in which the term is found.
     *  Terms are identified by the index at which its number appears in the
     *  term String array obtained from the <code>indexOf</code> method.
     *  May return null if positions have not been stored.
     */
    public int[] getTermPositions(int index);
  
    /**
     * Returns an array of TermVectorOffsetInfo in which the term is found.
     * May return null if offsets have not been stored.
     * 
     * @see org.apache.lucene.analysis.Token
     * 
     * @param index The position in the array to get the offsets from
     * @return An array of TermVectorOffsetInfo objects or the empty list
     */ 
    public TermVectorOffsetInfo [] getOffsets(int index);
}
