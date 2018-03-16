package org.apache.xpath.functions;


import javax.xml.transform.TransformerException;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the Concat() function.
 */
public class FuncConcat extends FunctionMultiArgs
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

    StringBuffer sb = new StringBuffer();

    sb.append(m_arg0.execute(xctxt).str());
    sb.append(m_arg1.execute(xctxt).str());

    if (null != m_arg2)
      sb.append(m_arg2.execute(xctxt).str());

    if (null != m_args)
    {
      for (int i = 0; i < m_args.length; i++)
      {
        sb.append(m_args[i].execute(xctxt).str());
      }
    }

    return new XString(sb.toString());
  }

  /**
   * Check that the number of arguments passed to this function is correct.
   *
   *
   * @param argNum The number of arguments that is being passed to the function.
   *
   * @throws WrongNumberArgsException
   */
  public void checkNumberArgs(int argNum) throws WrongNumberArgsException
  {
    if (argNum < 2)
      reportWrongNumberArgs();
  }

  /**
   * Constructs and throws a WrongNumberArgException with the appropriate
   * message for this function object.
   *
   * @throws WrongNumberArgsException
   */
  protected void reportWrongNumberArgs() throws WrongNumberArgsException {
      throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("gtone", null));
  }
}
