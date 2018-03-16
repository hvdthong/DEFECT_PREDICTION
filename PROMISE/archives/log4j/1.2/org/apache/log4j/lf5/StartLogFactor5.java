package org.apache.log4j.lf5;

import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

/**
 * Starts an instance of the LogFactor5 console for off-line viewing.
 *
 * @author Brad Marlborough
 * @author Richard Hurst
 */


public class StartLogFactor5 {





  /**
   * Main - starts a an instance of the LogFactor5 console and configures
   * the console settings.
   */
  public final static void main(String[] args) {

    LogBrokerMonitor monitor = new LogBrokerMonitor(
        LogLevel.getLog4JLevels());

    monitor.setFrameSize(LF5Appender.getDefaultMonitorWidth(),
        LF5Appender.getDefaultMonitorHeight());
    monitor.setFontSize(12);
    monitor.show();

  }




}


