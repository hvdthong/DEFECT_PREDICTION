package org.apache.log4j.spi;

import org.apache.log4j.spi.LoggerRepository;
import java.net.URL;

/**
   Implemented by classes capable of configuring log4j using a URL.
   
   @since 1.0
   @author Anders Kristensen
 */
public interface Configurator {

  /**
     <p><code>ENABLE_KEY</code> is the name of the constant
     holding the string value <b>log4j.enable</b>.

     <p>Setting the system property <b>log4j.disable</b> to DEBUG,
     INFO, WARN, ERROR or FATAL is equivalent to calling the {@link
     Hierarchy#disable} method with the corresponding level.

     @since 1.2 */


  /**
     Special level value signifying inherited behaviour. The
     current value of this string constant is <b>inherited</b>.
  */
  public static final String INHERITED = "inherited";



  /**
     Interpret a resource pointed by a URL and set up log4j accordingly.

     The configuration is done relative to the <code>hierarchy</code>
     parameter.

     @param url The URL to parse
     @param hierarchy The hierarchy to operation upon.
   */
  void doConfigure(URL url, LoggerRepository repository);
}
