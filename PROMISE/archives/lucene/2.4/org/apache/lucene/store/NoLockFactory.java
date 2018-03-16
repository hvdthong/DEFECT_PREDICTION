import java.io.IOException;

/**
 * Use this {@link LockFactory} to disable locking entirely.
 * This LockFactory is used when you call {@link FSDirectory#setDisableLocks}.
 * Only one instance of this lock is created.  You should call {@link
 * #getNoLockFactory()} to get the instance.
 *
 * @see LockFactory
 */

public class NoLockFactory extends LockFactory {

  private static NoLock singletonLock = new NoLock();
  private static NoLockFactory singleton = new NoLockFactory();

  public static NoLockFactory getNoLockFactory() {
    return singleton;
  }

  public Lock makeLock(String lockName) {
    return singletonLock;
  }

  public void clearLock(String lockName) {};
};

class NoLock extends Lock {
  public boolean obtain() throws IOException {
    return true;
  }

  public void release() {
  }

  public boolean isLocked() {
    return false;
  }

  public String toString() {
    return "NoLock";
  }
}
