package org.apache.log4j.helpers;

import java.net.URL;

/**
   Load resources (or images) from various sources.
 
  @author Sven Reimers
  @author Ceki G&uuml;lc&uuml;
 */

public class Loader  { 

  static String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
  
  /**
     This method will search for <code>resource</code> in different
     places. The rearch order is as follows:

     <ol>

     <p><li>Search for <code>fully/qualified/clazz/name/resource</code>
     with the {@link ClassLoader} that loaded <code>clazz</code>.

     <p><li>Search for <code>fully/qualified/clazz/name/resource</code>
     with the <code>null</code> (bootstrap) class loader.

     <p><li>Search for <code>resource</code> with the class loader
     that loaded <code>clazz</code>. 

     <pi><li>Try one last time with
     <code>ClassLoader.getSystemResource(resource)</code> 
     </ol>
     
     
  */
  static 
  public
  URL getResource(String resource, Class clazz) {
    
    URL url = null;


    LogLog.debug("Trying to find ["+resource+"] using Class.getResource().");
    
    
    
    try {
      url = clazz.getResource(resource);
      if(url != null) 
	return url;
    } catch (Throwable t) {
      LogLog.warn(TSTR,t);
    }
    
    
    String fullyQualified = resolveName(resource, clazz);
    LogLog.debug("Trying to find ["+fullyQualified+
		 "] using ClassLoader.getSystemResource().");
    url = ClassLoader.getSystemResource(fullyQualified);
    if(url != null) 
      return url;
    
    ClassLoader loader = clazz.getClassLoader();
    if(loader != null) {
      try {
	LogLog.debug("Trying to find ["+resource+"] using "+loader
		     +" class loader.");
	url = loader.getResource(resource); 
	if(url != null) 
	  return url;
      } catch(Throwable t) {
	LogLog.warn(TSTR, t);
      }
    }
    
    
    LogLog.debug("Trying to find ["+resource+"] using ClassLoader.getSystemResource().");
    url = ClassLoader.getSystemResource(resource);
    return url;

  }

  /**
     Add the fully qualified name of a class before resource (replace . with /).
   */
  static
  String resolveName(String resource, Class clazz) {
    String fqcn = clazz.getName();
    int index = fqcn.lastIndexOf('.');
    if (index != -1) {
      fqcn = fqcn.substring(0, index).replace('.', '/');
      resource = fqcn+"/"+resource;
    }
    return resource;
  }


}
