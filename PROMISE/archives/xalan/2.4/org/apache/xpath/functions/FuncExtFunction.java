package org.apache.xpath.functions;

import java.util.Vector;

import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.objects.*;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.extensions.ExtensionsTable;
import org.apache.xml.dtm.DTMIterator;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.traversal.NodeIterator;

import org.apache.xml.dtm.*;
import org.apache.xpath.axes.*;
import org.apache.xpath.res.XPATHErrorResources;
import org.apache.xalan.res.XSLMessages;

/**
 * <meta name="usage" content="advanced"/>
 * An object of this class represents an extension call expression.  When
 * the expression executes, it calls ExtensionsTable#extFunction, and then
 * converts the result to the appropriate XObject.
 */
public class FuncExtFunction extends Function
{

  /**
   * The namespace for the extension function, which should not normally
   *  be null or empty.
   *  @serial    
   */
  String m_namespace;

  /**
   * The local name of the extension.
   *  @serial   
   */
  String m_extensionName;

  /**
   * Unique method key, which is passed to ExtensionsTable#extFunction in
   *  order to allow caching of the method.
   *  @serial 
   */
  Object m_methodKey;

  /**
   * Array of static expressions which represent the parameters to the
   *  function.
   *  @serial   
   */
  Vector m_argVec = new Vector();

  /**
   * This function is used to fixup variables from QNames to stack frame
   * indexes at stylesheet build time.
   * @param vars List of QNames that correspond to variables.  This list
   * should be searched backwards for the first qualified name that
   * corresponds to the variable reference qname.  The position of the
   * QName in the vector from the start of the vector will be its position
   * in the stack frame (but variables above the globalsTop value will need
   * to be offset to the current stack frame).
   * NEEDSDOC @param globalsSize
   */
  public void fixupVariables(java.util.Vector vars, int globalsSize)
  {

    if (null != m_argVec)
    {
      int nArgs = m_argVec.size();

      for (int i = 0; i < nArgs; i++)
      {
        Expression arg = (Expression) m_argVec.elementAt(i);

        arg.fixupVariables(vars, globalsSize);
      }
    }
  }
  public String getNamespace()
  {
    return m_namespace;
  }
  public String getFunctionName()
  {
    return m_extensionName;
  }

  /**
   * Create a new FuncExtFunction based on the qualified name of the extension,
   * and a unique method key.
   *
   * @param namespace The namespace for the extension function, which should
   *                  not normally be null or empty.
   * @param extensionName The local name of the extension.
   * @param methodKey Unique method key, which is passed to
   *                  ExtensionsTable#extFunction in order to allow caching
   *                  of the method.
   */
  public FuncExtFunction(java.lang.String namespace,
                         java.lang.String extensionName, Object methodKey)
  {
    m_namespace = namespace;
    m_extensionName = extensionName;
    m_methodKey = methodKey;
  }

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt)
          throws javax.xml.transform.TransformerException
  {

    XObject result;
    Vector argVec = new Vector();
    int nArgs = m_argVec.size();

    for (int i = 0; i < nArgs; i++)
    {
      Expression arg = (Expression) m_argVec.elementAt(i);
      
      XObject xobj = arg.execute(xctxt);

      argVec.addElement(xobj);
    }
    ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
    Object val = extProvider.extFunction(m_namespace, m_extensionName, 
                                         argVec, m_methodKey);

    if (null != val)
    {
      result = XObject.create(val, xctxt);
    }
    else
    {
      result = new XNull();
    }

    return result;
  }

  /**
   * Set an argument expression for a function.  This method is called by the
   * XPath compiler.
   *
   * @param arg non-null expression that represents the argument.
   * @param argNum The argument number index.
   *
   * @throws WrongNumberArgsException If the argNum parameter is beyond what
   * is specified for this function.
   */
  public void setArg(Expression arg, int argNum)
          throws WrongNumberArgsException
  {
    m_argVec.addElement(arg);
  }

  /**
   * Check that the number of arguments passed to this function is correct.
   *
   *
   * @param argNum The number of arguments that is being passed to the function.
   *
   * @throws WrongNumberArgsException
   */
  public void checkNumberArgs(int argNum) throws WrongNumberArgsException{}

  /**
   * Constructs and throws a WrongNumberArgException with the appropriate
   * message for this function object.  This class supports an arbitrary
   * number of arguments, so this method must never be called.
   *
   * @throws WrongNumberArgsException
   */
  protected void reportWrongNumberArgs() throws WrongNumberArgsException {
    String fMsg = XSLMessages.createXPATHMessage(
        XPATHErrorResources.ER_INCORRECT_PROGRAMMER_ASSERTION,
        new Object[]{ "Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called." });

    throw new RuntimeException(fMsg);
  }
}
