package org.apache.log4j.lf5;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

import java.io.IOException;
import java.net.URL;

/**
 * The <code>DefaultLF5Configurator</code> provides a default
 * configuration for the <code>LF5Appender</code>.
 *
 * Note: The preferred method for configuring a <code>LF5Appender</code>
 * is to use the <code>LF5Manager</code> class. This class ensures
 * that configuration does not occur multiple times, and improves system
 * performance. Reconfiguring the monitor multiple times can result in
 * unexpected behavior.
 *
 * @author Brent Sprecher
 */


public class DefaultLF5Configurator implements Configurator {



  /**
   * This class should never be instantiated! It implements the <code>
   * Configurator</code>
   * interface, but does not provide the same functionality as full
   * configurator class.
   */
  private DefaultLF5Configurator() {

  }

  /**
   * This method configures the <code>LF5Appender</code> using a
   * default configuration file. The default configuration file is
   * <bold>defaultconfig.properties</bold>.
   * @throws java.io.IOException
   */
  public static void configure() throws IOException {
    String resource =
        "/org/apache/log4j/lf5/config/defaultconfig.properties";
    URL configFileResource =
        DefaultLF5Configurator.class.getResource(resource);

    if (configFileResource != null) {
      PropertyConfigurator.configure(configFileResource);
    } else {
      throw new IOException("Error: Unable to open the resource" +
          resource);
    }

  }

  /**
   * This is a dummy method that will throw an
   * <code>IllegalStateException</code> if used.
   */
  public void doConfigure(URL configURL, LoggerRepository repository) {
    throw new IllegalStateException("This class should NOT be" +
        " instantiated!");
  }




}
