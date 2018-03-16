public class LogDocMergePolicy extends LogMergePolicy {

  /** Default minimum segment size.  @see setMinMergeDocs */
  public static final int DEFAULT_MIN_MERGE_DOCS = 1000;

  public LogDocMergePolicy() {
    super();
    minMergeSize = DEFAULT_MIN_MERGE_DOCS;

    maxMergeSize = Long.MAX_VALUE;
  }
  protected long size(SegmentInfo info) {
    return info.docCount;
  }

  /** Sets the minimum size for the lowest level segments.
   * Any segments below this size are considered to be on
   * the same level (even if they vary drastically in size)
   * and will be merged whenever there are mergeFactor of
   * them.  This effectively truncates the "long tail" of
   * small segments that would otherwise be created into a
   * single level.  If you set this too large, it could
   * greatly increase the merging cost during indexing (if
   * you flush many small segments). */
  public void setMinMergeDocs(int minMergeDocs) {
    minMergeSize = minMergeDocs;
  }

  /** Get the minimum size for a segment to remain
   *  un-merged.
   *  @see #setMinMergeDocs **/
  public int getMinMergeDocs() {
    return (int) minMergeSize;
  }
}

