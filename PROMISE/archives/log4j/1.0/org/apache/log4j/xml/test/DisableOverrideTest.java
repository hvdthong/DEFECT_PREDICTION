package org.apache.log4j.xml.test;

import org.apache.log4j.Category;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.FileAppender;


public class DisableOverrideTest {

  static Category CAT = Category.getInstance(DisableOverrideTest.class.getName());

  public static void main( String[] argv) {

    String configFile = null;
    if(argv.length == 1) 
      configFile = argv[0];
    else 
      Usage("Wrong number of arguments.");
     
    DOMConfigurator.configure(configFile);
    DOMConfigurator.disableInfo();       
    CAT.debug("Hello world");
  }

  static
  void Usage(String msg) {
    System.err.println(msg);
    System.err.println("Usage: java "+ DisableOverrideTest.class.getName() +
		       "configFile");
    System.exit(1);
  }

}
