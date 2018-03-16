package org.apache.lucene.store;

import java.io.IOException;

/**
 * This exception is thrown when the <code>write.lock</code>
 * could not be acquired.  This
 * happens when a writer tries to open an index
 * that another writer already has open.
 * @see Lock#obtain(long).
 */
public class LockObtainFailedException extends IOException {
  public LockObtainFailedException(String message) {
    super(message);
  }
}
