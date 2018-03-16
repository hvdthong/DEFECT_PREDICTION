package org.apache.xpath.functions;


import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

import org.apache.xml.utils.XMLString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the SubstringAfter() function.
 */
public class FuncSubstringAfter extends Function2Args
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

    XMLString s1 = m_arg0.execute(xctxt).xstr();
    XMLString s2 = m_arg1.execute(xctxt).xstr();
    int index = s1.indexOf(s2);

    return (-1 == index)
           ? XString.EMPTYSTRING
           : (XString)s1.substring(index + s2.length());
  }
}
