package org.apache.xalan.templates;

import java.util.Vector;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMManager;

import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.functions.Function;
import org.apache.xpath.functions.Function2Args;
import org.apache.xpath.functions.WrongNumberArgsException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.SourceTreeManager;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xpath.XPathContext;
import org.apache.xalan.transformer.TransformerImpl;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;

import javax.xml.transform.TransformerException;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;

import org.apache.xml.utils.SAXSourceLocator;
import org.apache.xml.utils.XMLString;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the Doc() function.
 *
 * When the document function has exactly one argument and the argument
 * is a node-set, then the result is the union, for each node in the
 * argument node-set, of the result of calling the document function with
 * the first argument being the string-value of the node, and the second
 * argument being a node-set with the node as its only member. When the
 * document function has two arguments and the first argument is a node-set,
 * then the result is the union, for each node in the argument node-set,
 * of the result of calling the document function with the first argument
 * being the string-value of the node, and with the second argument being
 * the second argument passed to the document function.
 */
public class FuncDocument extends Function2Args
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
    int context = xctxt.getCurrentNode();
    DTM dtm = xctxt.getDTM(context);
    
    int docContext = dtm.getDocumentRoot(context);
    XObject arg = (XObject) this.getArg0().execute(xctxt);

    String base = "";
    Expression arg1Expr = this.getArg1();

    if (null != arg1Expr)
    {

      XObject arg2 = arg1Expr.execute(xctxt);

      if (XObject.CLASS_NODESET == arg2.getType())
      {
        int baseNode = arg2.iter().nextNode();

        if (baseNode == DTM.NULL)
          warn(xctxt, XSLTErrorResources.WG_EMPTY_SECOND_ARG, null);
        
        DTM baseDTM = xctxt.getDTM(baseNode);
        base = baseDTM.getDocumentBaseURI();

      }
      else
      {
        base = arg2.str();
      }
    }
    else
    {

      assertion(null != xctxt.getNamespaceContext(), "Namespace context can not be null!");
      base = xctxt.getNamespaceContext().getBaseIdentifier();
    }

    XNodeSet nodes = new XNodeSet(xctxt.getDTMManager());
    NodeSetDTM mnl = nodes.mutableNodeset();
    DTMIterator iterator = (XObject.CLASS_NODESET == arg.getType())
                            ? arg.iter() : null;
    int pos = DTM.NULL;

    while ((null == iterator) || (DTM.NULL != (pos = iterator.nextNode())))
    {
      XMLString ref = (null != iterator)
                   ? xctxt.getDTM(pos).getStringValue(pos) : arg.xstr();
      
      if (null == arg1Expr && DTM.NULL != pos)
      {
        DTM baseDTM = xctxt.getDTM(pos);
        base = baseDTM.getDocumentBaseURI();
      }

      if (null == ref)
        continue;

      if (DTM.NULL == docContext)
      {
      }

      int indexOfColon = ref.indexOf(':');
      int indexOfSlash = ref.indexOf('/');

      if ((indexOfColon != -1) && (indexOfSlash != -1)
              && (indexOfColon < indexOfSlash))
      {

        base = null;
      }

      int newDoc = getDoc(xctxt, context, ref.toString(), base);

      if (DTM.NULL != newDoc)
      {
        if (!mnl.contains(newDoc))
        {
          mnl.addElement(newDoc);
        }
      }

      if (null == iterator || newDoc == DTM.NULL)
        break;
    }

    return nodes;
  }

  /**
   * Get the document from the given URI and base
   *
   * @param xctxt The XPath runtime state.
   * @param context The current context node
   * @param uri Relative(?) URI of the document
   * @param base Base to resolve relative URI from.
   *
   * @return The document Node pointing to the document at the given URI
   * or null
   *
   * @throws javax.xml.transform.TransformerException
   */
  int getDoc(XPathContext xctxt, int context, String uri, String base)
          throws javax.xml.transform.TransformerException
  {

    SourceTreeManager treeMgr = xctxt.getSourceTreeManager();
    Source source;
   
    int newDoc;
    try
    {
      source = treeMgr.resolveURI(base, uri, xctxt.getSAXLocator());
      newDoc = treeMgr.getNode(source);
    }
    catch (IOException ioe)
    {
      throw new TransformerException(ioe.getMessage(), 
        (SourceLocator)xctxt.getSAXLocator(), ioe);
    }
    catch(TransformerException te)
    {
      throw new TransformerException(te);
    }

    if (DTM.NULL != newDoc)
      return newDoc;

    if (uri.length() == 0)
    {
      uri = xctxt.getNamespaceContext().getBaseIdentifier();
      try
      {
        source = treeMgr.resolveURI(base, uri, xctxt.getSAXLocator());
      }
      catch (IOException ioe)
      {
        throw new TransformerException(ioe.getMessage(), 
          (SourceLocator)xctxt.getSAXLocator(), ioe);
      }
    }

    String diagnosticsString = null;

    try
    {
      if ((null != uri) && (uri.toString().length() > 0))
      {
        newDoc = treeMgr.getSourceTree(source, xctxt.getSAXLocator(), xctxt);

      }
      else
        warn(xctxt, XSLTErrorResources.WG_CANNOT_MAKE_URL_FROM,
    }
    catch (Throwable throwable)
    {

      newDoc = DTM.NULL;

      while (throwable
             instanceof org.apache.xml.utils.WrappedRuntimeException)
      {
        throwable =
          ((org.apache.xml.utils.WrappedRuntimeException) throwable).getException();
      }

      if ((throwable instanceof NullPointerException)
              || (throwable instanceof ClassCastException))
      {
        throw new org.apache.xml.utils.WrappedRuntimeException(
          (Exception) throwable);
      }

      StringWriter sw = new StringWriter();
      PrintWriter diagnosticsWriter = new PrintWriter(sw);

      if (throwable instanceof TransformerException)
      {
        TransformerException spe = (TransformerException) throwable;

        {
          Throwable e = spe;

          while (null != e)
          {
            if (null != e.getMessage())
            {
              diagnosticsWriter.println(" (" + e.getClass().getName() + "): "
                                        + e.getMessage());
            }

            if (e instanceof TransformerException)
            {
              TransformerException spe2 = (TransformerException) e;

              SourceLocator locator = spe2.getLocator();
              if ((null != locator) && (null != locator.getSystemId()))
                diagnosticsWriter.println("   ID: " + locator.getSystemId()
                                          + " Line #" + locator.getLineNumber()
                                          + " Column #"
                                          + locator.getColumnNumber());

              e = spe2.getException();

              if (e instanceof org.apache.xml.utils.WrappedRuntimeException)
                e = ((org.apache.xml.utils.WrappedRuntimeException) e).getException();
            }
            else
              e = null;
          }
        }
      }
      else
      {
        diagnosticsWriter.println(" (" + throwable.getClass().getName()
                                  + "): " + throwable.getMessage());
      }

    }

    if (DTM.NULL == newDoc)
    {

      if (null != diagnosticsString)
      {
        warn(xctxt, XSLTErrorResources.WG_CANNOT_LOAD_REQUESTED_DOC,
      }
      else
        warn(xctxt, XSLTErrorResources.WG_CANNOT_LOAD_REQUESTED_DOC,
             new Object[]{
               uri == null
    }
    else
    {
    }

    return newDoc;
  }

  /**
   * Tell the user of an error, and probably throw an
   * exception.
   *
   * @param xctxt The XPath runtime state.
   * @param msg The error message code
   * @param args Arguments to be used in the error message
   * @throws XSLProcessorException thrown if the active ProblemListener and XPathContext decide
   * the error condition is severe enough to halt processing.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void error(XPathContext xctxt, int msg, Object args[])
          throws javax.xml.transform.TransformerException
  {

    String formattedMsg = XSLMessages.createMessage(msg, args);
    ErrorListener errHandler = xctxt.getErrorListener();
    TransformerException spe = new TransformerException(formattedMsg,
                              (SourceLocator)xctxt.getSAXLocator());

    if (null != errHandler)
      errHandler.error(spe);
    else
      System.out.println(formattedMsg);
  }

  /**
   * Warn the user of a problem.
   *
   * @param xctxt The XPath runtime state.
   * @param msg Warning message code
   * @param args Arguments to be used in the warning message
   * @throws XSLProcessorException thrown if the active ProblemListener and XPathContext decide
   * the error condition is severe enough to halt processing.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void warn(XPathContext xctxt, int msg, Object args[])
          throws javax.xml.transform.TransformerException
  {

    String formattedMsg = XSLMessages.createWarning(msg, args);
    ErrorListener errHandler = xctxt.getErrorListener();
    TransformerException spe = new TransformerException(formattedMsg,
                              (SourceLocator)xctxt.getSAXLocator());

    if (null != errHandler)
      errHandler.warning(spe);
    else
      System.out.println(formattedMsg);
  }

 /**
   * Overide the superclass method to allow one or two arguments.
   *
   *
   * @param argNum Number of arguments passed in to this function
   *
   * @throws WrongNumberArgsException
   */
  public void checkNumberArgs(int argNum) throws WrongNumberArgsException
  {
    if ((argNum < 1) || (argNum > 2))
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
  
  /**
   * Tell if the expression is a nodeset expression.  In other words, tell 
   * if you can execute {@link asNode() asNode} without an exception.
   * @return true if the expression can be represented as a nodeset.
   */
  public boolean isNodesetExpr()
  {
    return true;
  }

}
