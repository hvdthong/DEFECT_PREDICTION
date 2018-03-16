package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;

import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the GenerateId() function.
 */
public class FuncGenerateId extends FunctionDef1Arg
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

    int which = getArg0AsNode(xctxt);

    if (DTM.NULL != which)
    {
      return new XString("N" + Integer.toHexString(which).toUpperCase());
    }
    else
      return XString.EMPTYSTRING;
  }
}
