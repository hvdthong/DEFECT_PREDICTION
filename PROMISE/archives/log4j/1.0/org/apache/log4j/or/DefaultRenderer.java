package org.apache.log4j.or;

/**
   The default Renderer renders objects by calling their
   <code>toString</code> method.

   @author Ceki G&uuml;lc&uuml;
   @since 1.0 */
class DefaultRenderer implements ObjectRenderer {
  
  DefaultRenderer() {
  }

  /**
     Render the the object passed as parameter by calling its
     <code>toString</code> method.  */
  public
  String doRender(Object o) {
    return o.toString();
  }
}  
