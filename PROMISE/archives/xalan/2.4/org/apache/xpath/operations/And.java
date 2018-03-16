package org.apache.xpath.operations;

import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XBoolean;

import org.w3c.dom.Node;

/**
 * The 'and' operation expression executer.
 */
public class And extends Operation
{

  /**
   * AND two expressions and return the boolean result. Override
   * superclass method for optimization purposes.
   *
   * @param xctxt The runtime execution context.
   *
   * @return {@link org.apache.xpath.objects.XBoolean#S_TRUE} or 
   * {@link org.apache.xpath.objects.XBoolean#S_FALSE}.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException
  {

    XObject expr1 = m_left.execute(xctxt);

    if (expr1.bool())
    {
      XObject expr2 = m_right.execute(xctxt);

      return expr2.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
    }
    else
      return XBoolean.S_FALSE;
  }
  
  /**
   * Evaluate this operation directly to a boolean.
   *
   * @param xctxt The runtime execution context.
   *
   * @return The result of the operation as a boolean.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public boolean bool(XPathContext xctxt)
          throws javax.xml.transform.TransformerException
  {
    return (m_left.bool(xctxt) && m_right.bool(xctxt));
  }

}
