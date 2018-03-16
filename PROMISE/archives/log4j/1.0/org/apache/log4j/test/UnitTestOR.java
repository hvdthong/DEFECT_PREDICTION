package org.apache.log4j.test;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestFailure;
import junit.framework.Test;

import java.io.Serializable;


/**
   Unit test the {@link ObjectRenderer}.
   @author Ceki G&uuml;lc&uuml;
   @since 1.0 */
public class UnitTestOR extends TestCase {

  static UTObjectRenderer aor;
  static UTObjectRenderer bor;
  static UTObjectRenderer xor;
  static UTObjectRenderer yor;

  static UTObjectRenderer oor;
  static UTObjectRenderer nor;
  static UTObjectRenderer ior;
  static UTObjectRenderer cor;
  static UTObjectRenderer sor;



  public UnitTestOR(String name) {
    super(name);
  }


  public
  void setUp() {
    aor = new UTObjectRenderer("A");
    bor = new UTObjectRenderer("B");
    xor = new UTObjectRenderer("X");    
    yor = new UTObjectRenderer("Y");    

    oor = new UTObjectRenderer("Object");
    nor = new UTObjectRenderer("Number");
    ior = new UTObjectRenderer("Integer");
    cor = new UTObjectRenderer("Comparable");
    sor = new UTObjectRenderer("Serializable");
  }

  public
  void test1() {
    RendererMap map = new RendererMap();
    ObjectRenderer dr = map.getDefaultRenderer();
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, dr);
  }

  public
  void test2() {
    RendererMap map = new RendererMap();
    map.put(Integer.class, ior);
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, ior);

  }

  public
  void test3() {
    RendererMap map = new RendererMap();
    map.put(Number.class, ior);
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, ior);
  }

  public
  void test4() {
    RendererMap map = new RendererMap();
    map.put(Object.class, oor);
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, oor);
  }

  public
  void test5() {
    RendererMap map = new RendererMap();

    map.put(Object.class, oor);
    map.put(Number.class, nor);
    map.put(Integer.class, ior);

    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, ior);
  }

  public
  void test6() {
    RendererMap map = new RendererMap();

    map.put(Object.class, oor);
    map.put(Number.class, nor);
 
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, nor);
  }

  public
  void test7() {
    RendererMap map = new RendererMap();
    map.put(Comparable.class, cor);
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, cor);
  }


  public
  void test8() {
    RendererMap map = new RendererMap();
    map.put(Serializable.class, sor); 
    ObjectRenderer r = map.get(Integer.class);
    assertEquals(r, sor);
  }

  public
  void test9() {
    RendererMap map = new RendererMap();
    map.put(Y.class, yor); 
    ObjectRenderer r = map.get(B.class);
    assertEquals(r, yor);
  }

  public
  void test10() {
    RendererMap map = new RendererMap();
    map.put(X.class, xor); 
    ObjectRenderer r = map.get(B.class);
    assertEquals(r, xor);
  }




  public
  static
  Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new UnitTestOR("test1"));
    suite.addTest(new UnitTestOR("test2"));
    suite.addTest(new UnitTestOR("test3"));
    suite.addTest(new UnitTestOR("test4"));
    suite.addTest(new UnitTestOR("test5"));
    suite.addTest(new UnitTestOR("test6"));
    suite.addTest(new UnitTestOR("test7"));
    suite.addTest(new UnitTestOR("test8"));
    suite.addTest(new UnitTestOR("test9"));
    suite.addTest(new UnitTestOR("test10"));
    return suite;
  }



}

class UTObjectRenderer implements ObjectRenderer {
  
  String name;

  UTObjectRenderer(String name) {
    this.name = name;
  }

  public
  String doRender(Object o) {
    return name;
  }

  public
  String toString() {
    return("UTObjectRenderer: "+name);
  }
}


interface X  {
}

interface Y extends X {
}


class A implements Y  {
}

class B extends A  {
}
