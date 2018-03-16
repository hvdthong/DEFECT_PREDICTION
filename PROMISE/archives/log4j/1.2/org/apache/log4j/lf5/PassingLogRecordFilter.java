package org.apache.log4j.lf5;


/**
 * An implementation of LogRecordFilter which always returns true.
 *
 * @author Richard Wan
 */


public class PassingLogRecordFilter implements LogRecordFilter {





  /**
   * @return true;
   */
  public boolean passes(LogRecord record) {
    return true;
  }

  /**
   * Does nothing.
   */
  public void reset() {
  }


}

