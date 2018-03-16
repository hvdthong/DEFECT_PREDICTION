package org.apache.xpath.functions;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

/**
 * Execute the StartsWith() function.
 * @xsl.usage advanced
 */
public class FuncStartsWith extends Function2Args
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
    return m_arg0.execute(xctxt).xstr().startsWith(m_arg1.execute(xctxt).xstr())
           ? XBoolean.S_TRUE : XBoolean.S_FALSE;
  }
}
