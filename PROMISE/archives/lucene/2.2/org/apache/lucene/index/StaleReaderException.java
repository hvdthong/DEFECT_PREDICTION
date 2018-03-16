package org.apache.lucene.index;

import java.io.IOException;

/**
 * This exception is thrown when an {@link IndexReader}
 * tries to make changes to the index (via {@link
 * IndexReader#deleteDocument}, {@link
 * IndexReader#undeleteAll} or {@link IndexReader#setNorm})
 * but changes have already been committed to the index
 * since this reader was instantiated.  When this happens
 * you must open a new reader on the current index to make
 * the changes.
 */
public class StaleReaderException extends IOException {
  public StaleReaderException(String message) {
    super(message);
  }
}
