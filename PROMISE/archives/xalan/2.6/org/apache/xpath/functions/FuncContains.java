package org.apache.xpath.functions;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

/**
 * Execute the Contains() function.
 * @xsl.usage advanced
 */
public class FuncContains extends Function2Args
{

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException
  {

    String s1 = m_arg0.execute(xctxt).str();
    String s2 = m_arg1.execute(xctxt).str();

    if (s1.length() == 0 && s2.length() == 0)
      return XBoolean.S_TRUE;

    int index = s1.indexOf(s2);

    return (index > -1) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
  }
}
