import java.io.File;
import java.io.IOException;

/**
 * <p>Implements {@link LockFactory} using {@link
 * File#createNewFile()}.  This is the default LockFactory
 * for {@link FSDirectory}.</p>
 *
 * <p><b>NOTE:</b> the <a target="_top"
 * for <code>File.createNewFile</code></a> contain a vague
 * yet spooky warning about not using the API for file
 * locking.  This warning was added due to <a target="_top"
 * bug</a>, and in fact the only known problem with using
 * this API for locking is that the Lucene write lock may
 * not be released when the JVM exits abnormally.</p>

 * <p>When this happens, a {@link LockObtainFailedException}
 * is hit when trying to create a writer, in which case you
 * need to explicitly clear the lock file first.  You can
 * either manually remove the file, or use the {@link
 * org.apache.lucene.index.IndexReader#unlock(Directory)}
 * API.  But, first be certain that no writer is in fact
 * writing to the index otherwise you can easily corrupt
 * your index.</p>
 *
 * <p>If you suspect that this or any other LockFactory is
 * not working properly in your environment, you can easily
 * test it by using {@link VerifyingLockFactory}, {@link
 * LockVerifyServer} and {@link LockStressTest}.</p>
 *
 * @see LockFactory
 */

public class SimpleFSLockFactory extends LockFactory {

  /**
   * Directory specified by <code>org.apache.lucene.lockDir</code>
   * system property.  If that is not set, then <code>java.io.tmpdir</code>
   * system property is used.
   */

  private File lockDir;

  /**
   * Create a SimpleFSLockFactory instance, with null (unset)
   * lock directory.  This is package-private and is only
   * used by FSDirectory when creating this LockFactory via
   * the System property
   * org.apache.lucene.store.FSDirectoryLockFactoryClass.
   */
  SimpleFSLockFactory() throws IOException {
    this((File) null);
  }

  /**
   * Instantiate using the provided directory (as a File instance).
   * @param lockDir where lock files should be created.
   */
  public SimpleFSLockFactory(File lockDir) throws IOException {
    setLockDir(lockDir);
  }

  /**
   * Instantiate using the provided directory name (String).
   * @param lockDirName where lock files should be created.
   */
  public SimpleFSLockFactory(String lockDirName) throws IOException {
    lockDir = new File(lockDirName);
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
  }

  public Lock makeLock(String lockName) {
    if (lockPrefix != null) {
      lockName = lockPrefix + "-" + lockName;
    }
    return new SimpleFSLock(lockDir, lockName);
  }

  public void clearLock(String lockName) throws IOException {
    if (lockDir.exists()) {
      if (lockPrefix != null) {
        lockName = lockPrefix + "-" + lockName;
      }
      File lockFile = new File(lockDir, lockName);
      if (lockFile.exists() && !lockFile.delete()) {
        throw new IOException("Cannot delete " + lockFile);
      }
    }
  }
};

class SimpleFSLock extends Lock {

  File lockFile;
  File lockDir;

  public SimpleFSLock(File lockDir, String lockFileName) {
    this.lockDir = lockDir;
    lockFile = new File(lockDir, lockFileName);
  }

  public boolean obtain() throws IOException {

    if (!lockDir.exists()) {
      if (!lockDir.mkdirs())
        throw new IOException("Cannot create directory: " +
                              lockDir.getAbsolutePath());
    } else if (!lockDir.isDirectory()) {
      throw new IOException("Found regular file where directory expected: " + 
                            lockDir.getAbsolutePath());
    }
    return lockFile.createNewFile();
  }

  public void release() throws LockReleaseFailedException {
    if (lockFile.exists() && !lockFile.delete())
      throw new LockReleaseFailedException("failed to delete " + lockFile);
  }

  public boolean isLocked() {
    return lockFile.exists();
  }

  public String toString() {
    return "SimpleFSLock@" + lockFile;
  }
}
