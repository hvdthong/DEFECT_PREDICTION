package org.apache.log4j.helpers;

import java.io.PrintWriter;

/**

   A PrintWriter used to print stack traces of exceptions.

   <p>It's output target is always a QuietWriter.
   
 */
public class TracerPrintWriter extends PrintWriter {

  protected QuietWriter qWriter;

  public
  TracerPrintWriter(QuietWriter qWriter) {
    super(qWriter);
    this.qWriter = qWriter;
  }

  final
  public
  void setQuietWriter(QuietWriter qWriter) {
    this.qWriter = qWriter;
  }
        
  public
  void println(Object o) {
    this.qWriter.write(o.toString());
    this.qWriter.write(org.apache.log4j.Layout.LINE_SEP);
  }

  public
  void println(char[] s) {
    this.println(new String(s));
  }

  public
  void println(String s) {
    this.qWriter.write(s);
    this.qWriter.write(org.apache.log4j.Layout.LINE_SEP);
  }
}

