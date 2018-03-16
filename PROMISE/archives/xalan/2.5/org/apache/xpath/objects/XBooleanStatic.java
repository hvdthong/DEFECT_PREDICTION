package org.apache.xpath.objects;

/**
 * <meta name="usage" content="internal"/>
 * This class doesn't have any XPathContext, so override
 * whatever to ensure it works OK.
 */
public class XBooleanStatic extends XBoolean
{

  /** The value of the object.
   *  @serial          */
  boolean m_val;

  /**
   * Construct a XBooleanStatic object.
   *
   * @param b The value of the object
   */
  public XBooleanStatic(boolean b)
  {

    super(b);

    m_val = b;
  }

  /**
   * Tell if two objects are functionally equal.
   *
   * @param obj2 Object to compare to this 
   *
   * @return True if the two objects are equal
   *
   * @throws javax.xml.transform.TransformerException
   */
  public boolean equals(XObject obj2)
  {
    try
    {
      return m_val == obj2.bool();
    }
    catch(javax.xml.transform.TransformerException te)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(te);
    }
  }
}
