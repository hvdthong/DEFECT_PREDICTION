public interface IndexCommitPoint {

  /**
   * Get the segments file (<code>segments_N</code>) associated 
   * with this commit point.
   */
  public String getSegmentsFileName();
  
  /**
   * Delete this commit point.
   * <p>
   * Upon calling this, the writer is notified that this commit 
   * point should be deleted. 
   * <p>
   * Decision that a commit-point should be deleted is taken by the {@link IndexDeletionPolicy} in effect
   * and therefore this should only be called by its {@link IndexDeletionPolicy#onInit onInit()} or 
   * {@link IndexDeletionPolicy#onCommit onCommit()} methods.
  */
  public void delete();
}
