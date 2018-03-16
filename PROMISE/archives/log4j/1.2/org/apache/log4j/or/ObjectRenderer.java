package org.apache.log4j.or;

/**
   Implement this interface in order to render objects as strings.

   @author Ceki G&uuml;lc&uuml;
   @since 1.0 */
public interface ObjectRenderer {

  /**
     Render the object passed as parameter as a String.
   */
  public
  String doRender(Object o);
}
