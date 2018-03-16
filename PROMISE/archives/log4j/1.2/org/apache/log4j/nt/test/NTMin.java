package org.apache.log4j.nt.test;


import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.nt.NTEventLogAppender;
import org.apache.log4j.Priority;
import org.apache.log4j.NDC;


public class NTMin {

  static Category cat = Category.getInstance(NTMin.class.getName());

  public
  static
  void main(String argv[]) {

    init();
      test("someHost");
  }


  static
  void Usage(String msg) {
    System.err.println(msg);
    System.err.println( "Usage: java " + NTMin.class + "");
    System.exit(1);
  }


  static
  void init() {

    BasicConfigurator.configure(new NTEventLogAppender());
  }

  static
  void test(String host) {
    NDC.push(host);
    int i  = 0;
    cat.debug( "Message " + i++);
    cat.info( "Message " + i++);
    cat.warn( "Message " + i++);
    cat.error( "Message " + i++);
    cat.log(Priority.FATAL, "Message " + i++);
    cat.debug("Message " + i++,  new Exception("Just testing."));
  }
}
