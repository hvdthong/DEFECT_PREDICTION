package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * Execute the LocalPart() function.
 * @xsl.usage advanced
 */
public class FuncLocalPart extends FunctionDef1Arg
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

    int context = getArg0AsNode(xctxt);
    if(DTM.NULL == context)
      return XString.EMPTYSTRING;
    DTM dtm = xctxt.getDTM(context);
    String s = (context != DTM.NULL) ? dtm.getLocalName(context) : "";
    if(s.startsWith("#") || s.equals("xmlns"))
      return XString.EMPTYSTRING;

    return new XString(s);
  }
}
