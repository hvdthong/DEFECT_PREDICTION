package org.apache.log4j.spi;

import org.apache.log4j.Hierarchy;
import java.net.URL;

/**
   Implemented by classes capable of configuring log4j using a URL.
   
   @since 1.0
   @author Anders Kristensen
 */
public interface Configurator {
  /**
     Interpret a resource pointed by a URL and set up log4j accordingly.

     The configuration is done relative to the <code>hierarchy</code>
     parameter.

     @param url The URL to parse
     @param hierarchy The hierarchy to operation upon.
   */
  void doConfigure(URL url, Hierarchy hierarchy);
}
