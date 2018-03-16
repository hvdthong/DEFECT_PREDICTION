package org.apache.lucene.store;

import java.io.IOException;

/**
 * This exception is thrown when the <code>write.lock</code>
 * could not be released.
 * @see Lock#release().
 */
public class LockReleaseFailedException extends IOException {
  public LockReleaseFailedException(String message) {
    super(message);
  }
}
