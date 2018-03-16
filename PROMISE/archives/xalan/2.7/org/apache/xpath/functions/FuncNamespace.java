package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * Execute the Namespace() function.
 * @xsl.usage advanced
 */
public class FuncNamespace extends FunctionDef1Arg
{
    static final long serialVersionUID = -4695674566722321237L;

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
    
    String s;
    if(context != DTM.NULL)
    {
      DTM dtm = xctxt.getDTM(context);
      int t = dtm.getNodeType(context);
      if(t == DTM.ELEMENT_NODE)
      {
        s = dtm.getNamespaceURI(context);
      }
      else if(t == DTM.ATTRIBUTE_NODE)
      {


        s = dtm.getNodeName(context);
        if(s.startsWith("xmlns:") || s.equals("xmlns"))
          return XString.EMPTYSTRING;

        s = dtm.getNamespaceURI(context);
      }
      else
        return XString.EMPTYSTRING;
    }
    else 
      return XString.EMPTYSTRING;
    
    return ((null == s) ? XString.EMPTYSTRING : new XString(s));
  }
}
