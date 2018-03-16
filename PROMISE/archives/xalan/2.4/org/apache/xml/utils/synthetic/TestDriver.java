package org.apache.xml.utils.synthetic;

import org.apache.xml.utils.synthetic.Class;
import org.apache.xml.utils.synthetic.reflection.*;

/**
 * <meta name="usage" content="internal"/>
 * Class TestDriver <needs-comment/>
 */
public class TestDriver
{

  /** Field sampleField          */
  public static int sampleField = 32;

  /** Field inTest          */
  private boolean inTest = false;

  /**
   * Method main 
   *
   *
   * @param args
   */
  public static void main(String[] args)
  {

    try
    {
      System.out.println("Proxying java.awt.Frame...");

      Class myC = Class.forName("java.awt.Frame");

      myC.toSource(System.out, 0);
      System.out.println(
        "\nProxying org.apache.xml.utils.synthetic.TestDriver...");

      myC =
        Class.forName("com.ibm.org.apache.xml.utils.synthetic.TestDriver");

      myC.toSource(System.out, 0);
    }
    catch (ClassNotFoundException e)
    {
      System.out.println("Couldn't proxy: ");
      e.printStackTrace();
    }

    try
    {
      System.out.println("\nBuild a new beast...");

      Class myC = Class.declareClass(
        "com.ibm.org.apache.xml.utils.synthetic.BuildMe");
      Class inner = myC.declareInnerClass("island");

      inner.addExtends(Class.forName("java.lang.String"));

      Method m = inner.declareMethod("getValue");

      m.setReturnType(Class.forName("java.lang.String"));
      m.getBody().append("return toString();");
      myC.toSource(System.out, 0);
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (SynthesisException e)
    {
      e.printStackTrace();
    }
    catch (IllegalStateException e)
    {
      System.out.println("Unwritten function: " + e);
      e.printStackTrace();
    }
  }

  /**
   * Method dumpClass 
   *
   *
   * @param C
   */
  public static void dumpClass(Class C)
  {

    System.out.println("toString(): " + C);
    System.out.println("\tisPrimitive(): " + C.isPrimitive());
    System.out.println("\tisInterface(): " + C.isInterface());
    System.out.println("\tisInstance(\"foo\"): " + C.isInstance("foo"));
    System.out.println("\tisArray(): " + C.isArray());
    System.out.println("\tgetRealClass(): " + C.getRealClass());
  }

  /**
   * Method quickcheck 
   *
   */
  public void quickcheck()
  {

    Inner a = new Inner();

    a.setTest(!a.getTest());
  }

  /**
   * <meta name="usage" content="internal"/>
   * Class Inner <needs-comment/>
   */
  private class Inner
  {

    /**
     * Method getTest 
     *
     *
     * @return
     */
    public boolean getTest()
    {
      return inTest;
    }

    /**
     * Method setTest 
     *
     *
     * @param test
     */
    public void setTest(boolean test)
    {
      inTest = test;
    }
  }
}
