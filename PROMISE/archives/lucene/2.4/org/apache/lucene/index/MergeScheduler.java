import java.io.IOException;

/** <p>Expert: {@link IndexWriter} uses an instance
 *  implementing this interface to execute the merges
 *  selected by a {@link MergePolicy}.  The default
 *  MergeScheduler is {@link ConcurrentMergeScheduler}.</p>
 * <p><b>NOTE:</b> This API is new and still experimental
 * (subject to change suddenly in the next release)</p>
*/

public abstract class MergeScheduler {

  /** Run the merges provided by {@link IndexWriter#getNextMerge()}. */
  abstract void merge(IndexWriter writer)
    throws CorruptIndexException, IOException;

  /** Close this MergeScheduler. */
  abstract void close()
    throws CorruptIndexException, IOException;
}
