package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * <meta name="usage" content="advanced"/>
 */
public class FuncUnparsedEntityURI extends FunctionOneArg
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

    String name = m_arg0.execute(xctxt).str();
    int context = xctxt.getCurrentNode();
    DTM dtm = xctxt.getDTM(context);
    int doc = dtm.getDocument();
    
    String uri = dtm.getUnparsedEntityURI(name);

    return new XString(uri);
  }
}
