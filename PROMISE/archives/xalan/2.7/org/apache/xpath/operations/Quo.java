package org.apache.xpath.operations;

import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

/**
 * The 'quo' operation expression executer. (no longer supported by XPath).
 * @deprecated
 */
public class Quo extends Operation
{
    static final long serialVersionUID = 693765299196169905L;


  /**
   * Apply the operation to two operands, and return the result.
   *
   *
   * @param left non-null reference to the evaluated left operand.
   * @param right non-null reference to the evaluated right operand.
   *
   * @return non-null reference to the XObject that represents the result of the operation.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject operate(XObject left, XObject right)
          throws javax.xml.transform.TransformerException
  {
    return new XNumber((int) (left.num() / right.num()));
  }
}
