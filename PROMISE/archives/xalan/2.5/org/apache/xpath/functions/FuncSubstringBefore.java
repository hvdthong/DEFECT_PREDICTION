package org.apache.xpath.functions;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the SubstringBefore() function.
 */
public class FuncSubstringBefore extends Function2Args
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

    String s1 = m_arg0.execute(xctxt).str();
    String s2 = m_arg1.execute(xctxt).str();
    int index = s1.indexOf(s2);

    return (-1 == index)
           ? XString.EMPTYSTRING : new XString(s1.substring(0, index));
  }
}
