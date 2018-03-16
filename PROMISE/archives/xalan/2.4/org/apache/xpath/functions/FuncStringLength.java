package org.apache.xpath.functions;

import org.apache.xpath.res.XPATHErrorResources;


import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XNodeSet;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the StringLength() function.
 */
public class FuncStringLength extends FunctionDef1Arg
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
    return new XNumber(getArg0AsString(xctxt).length());
  }
}
