import java.io.IOException;
import java.util.HashSet;

/**
 * Implements {@link LockFactory} for a single in-process instance,
 * meaning all locking will take place through this one instance.
 * Only use this {@link LockFactory} when you are certain all
 * IndexReaders and IndexWriters for a given index are running
 * against a single shared in-process Directory instance.  This is
 * currently the default locking for RAMDirectory.
 *
 * @see LockFactory
 */

public class SingleInstanceLockFactory extends LockFactory {

  private HashSet locks = new HashSet();

  public Lock makeLock(String lockName) {
    return new SingleInstanceLock(locks, lockName);
  }

  public void clearLock(String lockName) throws IOException {
    synchronized(locks) {
      if (locks.contains(lockName)) {
        locks.remove(lockName);
      }
    }
  }
};

class SingleInstanceLock extends Lock {

  String lockName;
  private HashSet locks;

  public SingleInstanceLock(HashSet locks, String lockName) {
    this.locks = locks;
    this.lockName = lockName;
  }

  public boolean obtain() throws IOException {
    synchronized(locks) {
      return locks.add(lockName);
    }
  }

  public void release() {
    synchronized(locks) {
      locks.remove(lockName);
    }
  }

  public boolean isLocked() {
    synchronized(locks) {
      return locks.contains(lockName);
    }
  }

  public String toString() {
      return "SingleInstanceLock: " + lockName;
  }
}
