package org.apache.xalan.templates;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.functions.Function3Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the FormatNumber() function.
 */
public class FuncFormatNumb extends Function3Args
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

    ElemTemplateElement templElem =
      (ElemTemplateElement) xctxt.getNamespaceContext();
    StylesheetRoot ss = templElem.getStylesheetRoot();
    java.text.DecimalFormat formatter = null;
    java.text.DecimalFormatSymbols dfs = null;
    double num = getArg0().execute(xctxt).num();
    String patternStr = getArg1().execute(xctxt).str();

    if (patternStr.indexOf(0x00A4) > 0)

    try
    {
      Expression arg2Expr = getArg2();

      if (null != arg2Expr)
      {
        String dfName = arg2Expr.execute(xctxt).str();
        QName qname = new QName(dfName, xctxt.getNamespaceContext());

        dfs = ss.getDecimalFormatComposed(qname);

        if (null == dfs)
        {
          warn(xctxt, XSLTErrorResources.WG_NO_DECIMALFORMAT_DECLARATION,

        }
        else
        {

          formatter = new java.text.DecimalFormat();

          formatter.setDecimalFormatSymbols(dfs);
          formatter.applyLocalizedPattern(patternStr);
        }
      }

      if (null == formatter)
      {

        if (ss.getDecimalFormatCount() > 0)
          dfs = ss.getDecimalFormatComposed(new QName(""));

        if (dfs != null)
        {
          formatter = new java.text.DecimalFormat();

          formatter.setDecimalFormatSymbols(dfs);
          formatter.applyLocalizedPattern(patternStr);
        }
        else
        {
          dfs = new java.text.DecimalFormatSymbols(java.util.Locale.US);

          dfs.setInfinity(Constants.ATTRVAL_INFINITY);
          dfs.setNaN(Constants.ATTRVAL_NAN);

          formatter = new java.text.DecimalFormat();

          formatter.setDecimalFormatSymbols(dfs);

          if (null != patternStr)
            formatter.applyLocalizedPattern(patternStr);
        }
      }

      return new XString(formatter.format(num));
    }
    catch (Exception iae)
    {
      templElem.error(XSLTErrorResources.ER_MALFORMED_FORMAT_STRING,
                      new Object[]{ patternStr });

      return XString.EMPTYSTRING;

    }
  }

  /**
   * Warn the user of a problem.
   *
   * @param xctxt The XPath runtime state.
   * @param msg Warning message key
   * @param args Arguments to be used in warning message
   * @throws XSLProcessorException thrown if the active ProblemListener and XPathContext decide
   * the error condition is severe enough to halt processing.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void warn(XPathContext xctxt, String msg, Object args[])
          throws javax.xml.transform.TransformerException
  {

    String formattedMsg = XSLMessages.createWarning(msg, args);
    ErrorListener errHandler = xctxt.getErrorListener();

    errHandler.warning(new TransformerException(formattedMsg,
                                             (SAXSourceLocator)xctxt.getSAXLocator()));
  }

  /**
   * Overide the superclass method to allow one or two arguments. 
   *
   *
   * @param argNum Number of arguments passed in
   *
   * @throws WrongNumberArgsException
   */
  public void checkNumberArgs(int argNum) throws WrongNumberArgsException
  {
    if ((argNum > 3) || (argNum < 2))
      reportWrongNumberArgs();
  }

  /**
   * Constructs and throws a WrongNumberArgException with the appropriate
   * message for this function object.
   *
   * @throws WrongNumberArgsException
   */
  protected void reportWrongNumberArgs() throws WrongNumberArgsException {
  }
}
