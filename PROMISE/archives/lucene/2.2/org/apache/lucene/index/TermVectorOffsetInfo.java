public class TermVectorOffsetInfo {
  /**
   * Convenience declaration when creating a {@link org.apache.lucene.index.TermPositionVector} that stores only position information.
   */
  public static final TermVectorOffsetInfo[] EMPTY_OFFSET_INFO = new TermVectorOffsetInfo[0];
  private int startOffset;
  private int endOffset;

  public TermVectorOffsetInfo() {
  }

  public TermVectorOffsetInfo(int startOffset, int endOffset) {
    this.endOffset = endOffset;
    this.startOffset = startOffset;
  }

  /**
   * The accessor for the ending offset for the term
   * @return The offset
   */
  public int getEndOffset() {
    return endOffset;
  }

  public void setEndOffset(int endOffset) {
    this.endOffset = endOffset;
  }

  /**
   * The accessor for the starting offset of the term.
   *
   * @return The offset
   */
  public int getStartOffset() {
    return startOffset;
  }

  public void setStartOffset(int startOffset) {
    this.startOffset = startOffset;
  }

  /**
   * Two TermVectorOffsetInfos are equals if both the start and end offsets are the same
   * @param o The comparison Object
   * @return true if both {@link #getStartOffset()} and {@link #getEndOffset()} are the same for both objects.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TermVectorOffsetInfo)) return false;

    final TermVectorOffsetInfo termVectorOffsetInfo = (TermVectorOffsetInfo) o;

    if (endOffset != termVectorOffsetInfo.endOffset) return false;
    if (startOffset != termVectorOffsetInfo.startOffset) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = startOffset;
    result = 29 * result + endOffset;
    return result;
  }
}
