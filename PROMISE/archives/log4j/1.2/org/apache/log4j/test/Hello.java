package org.apache.log4j.test; 

import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;

/**
   Very simple log4j usage example.

   @author  Ceki G&uuml;lc&uuml;   
 */
public class Hello {

  static Category cat = Category.getInstance(Hello.class);

  public 
  static 
  void main(String argv[]) {
    BasicConfigurator.configure();
    cat.debug("Hello world.");
    cat.info("What a beatiful day.");
  }
}
