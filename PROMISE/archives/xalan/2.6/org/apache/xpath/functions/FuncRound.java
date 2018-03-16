package org.apache.xpath.functions;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

/**
 * Execute the round() function.
 * @xsl.usage advanced
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
          final XObject obj = m_arg0.execute(xctxt);
          final double val= obj.num();
          if (val >= -0.5 && val < 0) return new XNumber(-0.0);
          if (val == 0.0) return new XNumber(val);
          return new XNumber(java.lang.Math.floor(val
                                            + 0.5));
  }
}
