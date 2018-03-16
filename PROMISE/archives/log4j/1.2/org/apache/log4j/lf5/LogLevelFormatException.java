package org.apache.log4j.lf5;

/**
 * Thrown to indicate that the client has attempted to convert a string
 * to one the LogLevel types, but the string does not have the appropriate
 * format.
 *
 * @author Michael J. Sikorsky<
 * @author Robert Shaw
 */


public class LogLevelFormatException extends Exception {




  public LogLevelFormatException(String message) {
    super(message);
  }





}






