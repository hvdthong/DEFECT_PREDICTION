package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;

import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.DOMHelper;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XString;

import org.apache.xml.utils.XMLString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the Sum() function.
 */
public class FuncSum extends FunctionOneArg
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

    DTMIterator nodes = m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
    double sum = 0.0;
    int pos;

    while (DTM.NULL != (pos = nodes.nextNode()))
    {
      DTM dtm = nodes.getDTM(pos);
      XMLString s = dtm.getStringValue(pos);

      if (null != s)
        sum += s.toDouble();
    }
    nodes.detach();

    return new XNumber(sum);
  }
}
