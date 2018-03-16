package org.apache.log4j.lf5;


/**
 * An interface for classes which filters LogRecords.  Implementations
 * represent a rule or condition which LogRecords may pass or fail.
 * @see LogRecord
 *
 * @author Richard Wan
 */


public interface LogRecordFilter {


  /**
   * @return true if the specified LogRecord satisfies whatever condition
   * implementing class tests for.
   */
  public boolean passes(LogRecord record);

}

