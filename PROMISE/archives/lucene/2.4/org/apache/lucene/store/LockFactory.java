import java.io.IOException;

/**
 * <p>Base class for Locking implementation.  {@link Directory} uses
 * instances of this class to implement locking.</p>
 *
 * <p>Note that there are some useful tools to verify that
 * your LockFactory is working correctly: {@link
 * VerifyingLockFactory}, {@link LockStressTest}, {@link
 * LockVerifyServer}.</p>
 *
 * @see LockVerifyServer
 * @see LockStressTest
 * @see VerifyingLockFactory
 */

public abstract class LockFactory {

  protected String lockPrefix = "";

  /**
   * Set the prefix in use for all locks created in this
   * LockFactory.  This is normally called once, when a
   * Directory gets this LockFactory instance.  However, you
   * can also call this (after this instance is assigned to
   * a Directory) to override the prefix in use.  This
   * is helpful if you're running Lucene on machines that
   * have different mount points for the same shared
   * directory.
   */
  public void setLockPrefix(String lockPrefix) {
    this.lockPrefix = lockPrefix;
  }

  /**
   * Get the prefix in use for all locks created in this LockFactory.
   */
  public String getLockPrefix() {
    return this.lockPrefix;
  }

  /**
   * Return a new Lock instance identified by lockName.
   * @param lockName name of the lock to be created.
   */
  public abstract Lock makeLock(String lockName);

  /**
   * Attempt to clear (forcefully unlock and remove) the
   * specified lock.  Only call this at a time when you are
   * certain this lock is no longer in use.
   * @param lockName name of the lock to be cleared.
   */
  abstract public void clearLock(String lockName) throws IOException;
}
