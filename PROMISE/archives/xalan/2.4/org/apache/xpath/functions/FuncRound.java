package org.apache.xpath.functions;


import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNumber;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the round() function.
 */
public class FuncRound extends FunctionOneArg
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
    return new XNumber(java.lang.Math.floor(m_arg0.execute(xctxt).num()
                                            + 0.5));
  }
}
