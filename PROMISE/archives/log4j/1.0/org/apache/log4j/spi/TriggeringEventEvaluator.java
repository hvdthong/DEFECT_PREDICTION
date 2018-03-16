package org.apache.log4j.spi;

/**
   
   Implementions of this interface allow certain appenders to decide
   when to perform an appender specific action.

  <p>See {@link org.apache.log4j.net.SMTPAppender} for an example of
  an appender that depends on a
  <code>TriggeringEventEvaluators</code>.

  @author Ceki G&uuml;lc&uuml;
  @since version 1.0
   
 */
public interface TriggeringEventEvaluator {
  
  /**
     Is this the triggering event?
   */
  public boolean isTriggeringEvent(LoggingEvent event);
}
