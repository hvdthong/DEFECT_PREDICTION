import java.util.List;

/**
 * This {@link IndexDeletionPolicy} implementation that
 * keeps only the most recent commit and immediately removes
 * all prior commits after a new commit is done.  This is
 * the default deletion policy.
 */

public final class KeepOnlyLastCommitDeletionPolicy implements IndexDeletionPolicy {

  /**
   * Deletes all commits except the most recent one.
   */
  public void onInit(List commits) {
    onCommit(commits);
  }

  /**
   * Deletes all commits except the most recent one.
   */
  public void onCommit(List commits) {
    int size = commits.size();
    for(int i=0;i<size-1;i++) {
      ((IndexCommit) commits.get(i)).delete();
    }
  }
}
