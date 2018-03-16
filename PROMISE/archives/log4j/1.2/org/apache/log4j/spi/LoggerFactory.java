package org.apache.log4j.spi;

import org.apache.log4j.Logger;

/**
   
  Implement this interface to create new instances of Logger or
  a sub-class of Logger.

  <p>See <code>examples/subclass/MyLogger.java</code> for an example.

  @author Ceki G&uuml;lc&uuml;
  @since version 0.8.5
   
 */
public interface LoggerFactory {

  public
  Logger makeNewLoggerInstance(String name);

}
