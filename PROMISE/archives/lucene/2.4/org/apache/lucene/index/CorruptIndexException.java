package org.apache.lucene.index;

import java.io.IOException;

/**
 * This exception is thrown when Lucene detects
 * an inconsistency in the index.
 */
public class CorruptIndexException extends IOException {
  public CorruptIndexException(String message) {
    super(message);
  }
}
