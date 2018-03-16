package org.apache.log4j.lf5;

import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

/**
 * <code>AppenderFinalizer</code> has a single method that will finalize
 * resources associated with a <code>LogBrokerMonitor</code> in the event
 * that the <code>LF5Appender</code> class is destroyed, and the class loader
 * is garbage collected.
 *
 * @author Brent Sprecher
 */


public class AppenderFinalizer {


  protected LogBrokerMonitor _defaultMonitor = null;



  public AppenderFinalizer(LogBrokerMonitor defaultMonitor) {
    _defaultMonitor = defaultMonitor;
  }


  /**
   * @throws java.lang.Throwable
   */
  protected void finalize() throws Throwable {
    System.out.println("Disposing of the default LogBrokerMonitor instance");
    _defaultMonitor.dispose();
  }



}
