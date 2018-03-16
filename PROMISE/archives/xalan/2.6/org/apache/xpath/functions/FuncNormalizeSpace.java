package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.xml.sax.ContentHandler;

/**
 * Execute the normalize-space() function.
 * @xsl.usage advanced
 */
public class FuncNormalizeSpace extends FunctionDef1Arg
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
    XMLString s1 = getArg0AsString(xctxt);

    return (XString)s1.fixWhiteSpace(true, true, false);
  }
  
  /**
   * Execute an expression in the XPath runtime context, and return the 
   * result of the expression.
   *
   *
   * @param xctxt The XPath runtime context.
   *
   * @return The result of the expression in the form of a <code>XObject</code>.
   *
   * @throws javax.xml.transform.TransformerException if a runtime exception 
   *         occurs.
   */
  public void executeCharsToContentHandler(XPathContext xctxt, 
                                              ContentHandler handler)
    throws javax.xml.transform.TransformerException,
           org.xml.sax.SAXException
  {
    if(Arg0IsNodesetExpr())
    {
      int node = getArg0AsNode(xctxt);
      if(DTM.NULL != node)
      {
        DTM dtm = xctxt.getDTM(node);
        dtm.dispatchCharactersEvents(node, handler, true);
      }
    }
    else
    {
      XObject obj = execute(xctxt);
      obj.dispatchCharactersEvents(handler);
    }
  }

}
