package org.apache.xpath.operations;

import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

/**
 * The 'boolean()' operation expression executer.
 */
public class Bool extends UnaryOperation
{
    static final long serialVersionUID = 44705375321914635L;

  /**
   * Apply the operation to two operands, and return the result.
   *
   *
   * @param right non-null reference to the evaluated right operand.
   *
   * @return non-null reference to the XObject that represents the result of the operation.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject operate(XObject right) throws javax.xml.transform.TransformerException
  {

    if (XObject.CLASS_BOOLEAN == right.getType())
      return right;
    else
      return right.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
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
    return m_right.bool(xctxt);
  }

}
