package org.apache.xpath.functions;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

/**
 * Execute the StringLength() function.
 * @xsl.usage advanced
 */
public class FuncStringLength extends FunctionDef1Arg
{
    static final long serialVersionUID = -159616417996519839L;

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
    return new XNumber(getArg0AsString(xctxt).length());
  }
}
