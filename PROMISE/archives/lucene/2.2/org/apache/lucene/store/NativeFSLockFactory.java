import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

/**
 * Implements {@link LockFactory} using native OS file locks
 * (available through java.nio.*).  Note that for certain
 * filesystems native locks are possible but must be
 * explicity configured and enabled (and may be disabled by
 * default).  For example, for NFS servers there sometimes
 * must be a separate lockd process running, and other
 * configuration may be required such as running the server
 * in kernel mode.  Other filesystems may not even support
 * native OS locks in which case you must use a different
 * {@link LockFactory} implementation.
 *
 * <p>The advantage of this lock factory over
 * {@link SimpleFSLockFactory} is that the locks should be
 * "correct", whereas {@link SimpleFSLockFactory} uses
 * java.io.File.createNewFile which
 * using it for locking.  Furthermore, if the JVM crashes,
 * the OS will free any held locks, whereas
 * {@link SimpleFSLockFactory} will keep the locks held, requiring
 * manual removal before re-running Lucene.</p>
 *
 * <p>Note that, unlike {@link SimpleFSLockFactory}, the existence of
 * leftover lock files in the filesystem on exiting the JVM
 * is fine because the OS will free the locks held against
 * these files even though the files still remain.</p>
 *
 * @see LockFactory
 */

public class NativeFSLockFactory extends LockFactory {

  /**
   * Directory specified by <code>org.apache.lucene.lockDir</code>
   * system property.  If that is not set, then <code>java.io.tmpdir</code>
   * system property is used.
   */

  private File lockDir;

  private void acquireTestLock() throws IOException {
    String randomLockName = "lucene-" + Long.toString(new Random().nextInt(), Character.MAX_RADIX) + "-test.lock";
    
    Lock l = makeLock(randomLockName);
    try {
      l.obtain();
    } catch (IOException e) {
      IOException e2 = new IOException("Failed to acquire random test lock; please verify filesystem for lock directory '" + lockDir + "' supports locking");
      e2.initCause(e);
      throw e2;
    }

    l.release();
  }

  /**
   * Create a NativeFSLockFactory instance, with null (unset)
   * lock directory.  This is package-private and is only
   * used by FSDirectory when creating this LockFactory via
   * the System property
   * org.apache.lucene.store.FSDirectoryLockFactoryClass.
   */
  NativeFSLockFactory() throws IOException {
    this((File) null);
  }

  /**
   * Create a NativeFSLockFactory instance, storing lock
   * files into the specified lockDirName:
   *
   * @param lockDirName where lock files are created.
   */
  public NativeFSLockFactory(String lockDirName) throws IOException {
    this(new File(lockDirName));
  }

  /**
   * Create a NativeFSLockFactory instance, storing lock
   * files into the specified lockDir:
   * 
   * @param lockDir where lock files are created.
   */
  public NativeFSLockFactory(File lockDir) throws IOException {
    setLockDir(lockDir);
  }

  /**
   * Set the lock directory.  This is package-private and is
   * only used externally by FSDirectory when creating this
   * LockFactory via the System property
   * org.apache.lucene.store.FSDirectoryLockFactoryClass.
   */
  void setLockDir(File lockDir) throws IOException {
    this.lockDir = lockDir;
    if (lockDir != null) {
      if (!lockDir.exists()) {
        if (!lockDir.mkdirs())
          throw new IOException("Cannot create directory: " +
                                lockDir.getAbsolutePath());
      } else if (!lockDir.isDirectory()) {
        throw new IOException("Found regular file where directory expected: " + 
                              lockDir.getAbsolutePath());
      }

      acquireTestLock();
    }
  }

  public synchronized Lock makeLock(String lockName) {
    if (lockPrefix != null)
      lockName = lockPrefix + "-n-" + lockName;
    return new NativeFSLock(lockDir, lockName);
  }

  public void clearLock(String lockName) throws IOException {
    if (lockDir.exists()) {
      if (lockPrefix != null) {
        lockName = lockPrefix + "-n-" + lockName;
      }
      File lockFile = new File(lockDir, lockName);
      if (lockFile.exists() && !lockFile.delete()) {
        throw new IOException("Cannot delete " + lockFile);
      }
    }
  }
};

class NativeFSLock extends Lock {

  private RandomAccessFile f;
  private FileChannel channel;
  private FileLock lock;
  private File path;
  private File lockDir;

  /*
   * The javadocs for FileChannel state that you should have
   * a single instance of a FileChannel (per JVM) for all
   * locking against a given file.  To ensure this, we have
   * a single (static) HashSet that contains the file paths
   * of all currently locked locks.  This protects against
   * possible cases where different Directory instances in
   * one JVM (each with their own NativeFSLockFactory
   * instance) have set the same lock dir and lock prefix.
   */
  private static HashSet LOCK_HELD = new HashSet();

  public NativeFSLock(File lockDir, String lockFileName) {
    this.lockDir = lockDir;
    path = new File(lockDir, lockFileName);
  }

  public synchronized boolean obtain() throws IOException {

    if (isLocked()) {
      return false;
    }

    if (!lockDir.exists()) {
      if (!lockDir.mkdirs())
        throw new IOException("Cannot create directory: " +
                              lockDir.getAbsolutePath());
    } else if (!lockDir.isDirectory()) {
      throw new IOException("Found regular file where directory expected: " + 
                            lockDir.getAbsolutePath());
    }

    String canonicalPath = path.getCanonicalPath();

    boolean markedHeld = false;

    try {


      synchronized(LOCK_HELD) {
        if (LOCK_HELD.contains(canonicalPath)) {
          return false;
        } else {
          LOCK_HELD.add(canonicalPath);
          markedHeld = true;
        }
      }

      try {
        f = new RandomAccessFile(path, "rw");
      } catch (IOException e) {
        failureReason = e;
        f = null;
      }

      if (f != null) {
        try {
          channel = f.getChannel();
          try {
            lock = channel.tryLock();
          } catch (IOException e) {
            failureReason = e;
          } finally {
            if (lock == null) {
              try {
                channel.close();
              } finally {
                channel = null;
              }
            }
          }
        } finally {
          if (channel == null) {
            try {
              f.close();
            } finally {
              f = null;
            }
          }
        }
      }

    } finally {
      if (markedHeld && !isLocked()) {
        synchronized(LOCK_HELD) {
          if (LOCK_HELD.contains(canonicalPath)) {
            LOCK_HELD.remove(canonicalPath);
          }
        }
      }
    }
    return isLocked();
  }

  public synchronized void release() {
    try {
      if (isLocked()) {
        try {
          lock.release();
        } finally {
          lock = null;
          try {
            channel.close();
          } finally {
            channel = null;
            try {
              f.close();
            } finally {
              f = null;
              synchronized(LOCK_HELD) {
                LOCK_HELD.remove(path.getCanonicalPath());
              }
            }
          }
        }
        path.delete();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isLocked() {
    return lock != null;
  }

  public String toString() {
    return "NativeFSLock@" + path;
  }

  public void finalize() throws Throwable {
    try {
      if (isLocked()) {
        release();
      }
    } finally {
      super.finalize();
    }
  }
}
