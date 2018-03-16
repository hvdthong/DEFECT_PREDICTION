package org.apache.log4j.helpers;

import java.net.URL;

/**
   Load resources (or images) from various sources.
 
  @author Ceki G&uuml;lc&uuml;
 */

public class Loader  { 

  static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";

  static private boolean java1 = true;

  static {
    String prop = OptionConverter.getSystemProperty("java.version", null);
    
    if(prop != null) {
      int i = prop.indexOf('.');
      if(i != -1) {	
	if(prop.charAt(i+1) != '1')
	  java1 = false;
      } 
    }
  }

  /**
     This method will search for <code>resource</code> in different
     places. The rearch order is as follows:

     <ol>

     <p><li>Search for <code>resource</code> using the thread context
     class loader under Java2. If that fails, search for
     <code>resource</code> using the class loader that loaded this
     class (<code>Loader</code>). Under JDK 1.1, only the the class
     loader that loaded this class (<code>Loader</code>) is used.

     <p><li>Try one last time with
     <code>ClassLoader.getSystemResource(resource)</code>, that is is
     using the system class loader in JDK 1.2 and virtual machine's
     built-in class loader in JDK 1.1.

     </ol>
     
  */
  static
  public
  URL getResource(String resource) {
    ClassLoader classLoader = null;
    URL url = null;
    
    try {
      if(!java1) {
	classLoader = Thread.currentThread().getContextClassLoader();	
	if(classLoader != null) {
	  LogLog.debug("Trying to find ["+resource+"] using context classloader "
		       +classLoader+".");
	  url = classLoader.getResource(resource);      
	  if(url != null) {
	    return url;
	  }
	}
      }
      
      classLoader = Loader.class.getClassLoader(); 
      if(classLoader == null) {
	LogLog.warn("Loader.class.getClassLoader returned null!");
      } else {
	
	LogLog.debug("Trying to find ["+resource+"] using "+classLoader
		     +" class loader.");
	url = classLoader.getResource(resource);
	if(url != null) {
	  return url;
	}
      }
    } catch(Throwable t) {
      LogLog.warn(TSTR, t);
    }
    
    LogLog.debug("Trying to find ["+resource+
		 "] using ClassLoader.getSystemResource().");
    return ClassLoader.getSystemResource(resource);
  } 
  
  /**
     Are we running under JDK 1.x?        
  */
  public
  static
  boolean isJava1() {
    return java1;
  }
  
  
  /**
     Load the specified class using the <code>Thread</code>
     <code>contextClassLoader</code> if running under Java2 or current
     class loader if running under JDK 1.1.
  */
  static
  public 
  Class loadClass (Double clazz) throws ClassNotFoundException {
    return null;
  } 
}
