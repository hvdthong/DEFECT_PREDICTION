import java.io.IOException;

/** A {@link MergeScheduler} that simply does each merge
 *  sequentially, using the current thread. */
public class SerialMergeScheduler extends MergeScheduler {

  /** Just do the merges in sequence. We do this
   * "synchronized" so that even if the application is using
   * multiple threads, only one merge may run at a time. */
  synchronized public void merge(IndexWriter writer)
    throws CorruptIndexException, IOException {

    while(true) {
      MergePolicy.OneMerge merge = writer.getNextMerge();
      if (merge == null)
        break;
      writer.merge(merge);
    }
  }

  public void close() {}
}
