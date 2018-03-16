package org.apache.log4j.spi;

import java.io.Writer;
import java.io.PrintWriter;
import java.util.Vector;

/**
   


 */
public class ThrowableInformation implements java.io.Serializable {

  static final long serialVersionUID = -4748765566864322735L;

  private transient Throwable throwable;
  private String[] rep;
  
  public
  ThrowableInformation(Throwable throwable) {
    this.throwable = throwable;
  }

  public
  Throwable getThrowable() {
    return throwable;
  }
  
  public
  String[] getThrowableStrRep() {
    if(rep != null) {
      return (String[]) rep.clone();
    } else {
      VectorWriter vw = new VectorWriter();
      throwable.printStackTrace(vw);
      rep = vw.toStringArray();
      vw.clear();
      return rep;
    }
  }
}

class VectorWriter extends PrintWriter {
    
  private Vector v;
  
  VectorWriter() {
    super(new NullWriter());
    v = new Vector();
  }

  public
  void print(Object o) {      
    v.addElement(o.toString());
  }
  
  public
  void print(char[] s) {
    v.addElement(new String(s));
  }
  
  public
  void print(String s) {
    v.addElement(s);
  }

  public
  void println(Object o) {      
    v.addElement(o.toString());
  }
  
  public
  void println(char[] s) {
    v.addElement(new String(s));
  }
  
  public  
  void println(String s) {
    v.addElement(s);
  }

  public
  String[] toStringArray() {
    int len = v.size();
    String[] sa = new String[len];
    for(int i = 0; i < len; i++) {
      sa[i] = (String) v.elementAt(i);
    }
    return sa;
  }

  public
  void clear() {
    v.setSize(0);
  }
}  

class NullWriter extends Writer {    
  
  public 
  void close() {
  }

  public 
  void flush() {
  }

  public
  void write(char[] cbuf, int off, int len) {
  }
}

