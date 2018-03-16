package org.apache.xpath.functions;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * Execute the String() function.
 * @xsl.usage advanced
 */
public class FuncString extends FunctionDef1Arg
{
    static final long serialVersionUID = -2206677149497712883L;

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
    return (XString)getArg0AsString(xctxt);
  }
}
