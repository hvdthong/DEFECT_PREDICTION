package org.apache.xalan.extensions;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.functions.FuncExtFunction;

/**
 * <meta name="usage" content="internal"/>
 * Abstract base class for handling an extension namespace for XPath.
 * Provides functions to test a function's existence and call a function.
 * Also provides functions for calling an element and testing for
 * an element's existence.
 *
 * @author Sanjiva Weerawarana (sanjiva@watson.ibm.com)
 */
public abstract class ExtensionHandler
{

  /** uri of the extension namespace          */
  protected String m_namespaceUri; 

  /** scripting language of implementation          */
  protected String m_scriptLang;

  /** a zero length Object array used in getClassForName() */
  private static final Object NO_OBJS[] = new Object[0];

  /** the Method object for getContextClassLoader */
  private static Method getCCL;

  static
  {
    try
    {
      getCCL = Thread.class.getMethod("getContextClassLoader", new Class[0]);
    }
    catch (Exception e)
    {
      getCCL = null;
    }
  }

  /**
   * Replacement for Class.forName.  This method loads a class using the context class loader
   * if we're running under Java2 or higher.  If we're running under Java1, this
   * method just uses Class.forName to load the class.
   * 
   * @param className Name of the class to load
   */
  public static Class getClassForName(String className)
      throws ClassNotFoundException
  {
    Class result = null;
    
    if(className.equals("org.apache.xalan.xslt.extensions.Redirect"))
      className = "org.apache.xalan.lib.Redirect";
      
    if (getCCL != null)
    {
      try {
        ClassLoader contextClassLoader =
                              (ClassLoader) getCCL.invoke(Thread.currentThread(), NO_OBJS);
        result = contextClassLoader.loadClass(className);
      }
      catch (ClassNotFoundException cnfe)
      {
        result = Class.forName(className);
      }
      catch (Exception e)
      {
        getCCL = null;
        result = Class.forName(className);
      }
    }

    else
       result = Class.forName(className);

    return result;
 }


  /**
   * Construct a new extension namespace handler given all the information
   * needed.
   *
   * @param namespaceUri the extension namespace URI that I'm implementing
   * @param scriptLang   language of code implementing the extension
   */
  protected ExtensionHandler(String namespaceUri, String scriptLang)
  {
    m_namespaceUri = namespaceUri;
    m_scriptLang = scriptLang;
  }

  /**
   * Tests whether a certain function name is known within this namespace.
   * @param function name of the function being tested
   * @return true if its known, false if not.
   */
  public abstract boolean isFunctionAvailable(String function);

  /**
   * Tests whether a certain element name is known within this namespace.
   * @param function name of the function being tested
   *
   * @param element Name of element to check
   * @return true if its known, false if not.
   */
  public abstract boolean isElementAvailable(String element);

  /**
   * Process a call to a function.
   *
   * @param funcName Function name.
   * @param args     The arguments of the function call.
   * @param methodKey A key that uniquely identifies this class and method call.
   * @param exprContext The context in which this expression is being executed.
   *
   * @return the return value of the function evaluation.
   *
   * @throws TransformerException          if parsing trouble
   */
  public abstract Object callFunction(
    String funcName, Vector args, Object methodKey,
      ExpressionContext exprContext) throws TransformerException;

  /**
   * Process a call to a function.
   *
   * @param extFunction The XPath extension function.
   * @param args     The arguments of the function call.
   * @param exprContext The context in which this expression is being executed.
   *
   * @return the return value of the function evaluation.
   *
   * @throws TransformerException          if parsing trouble
   */
  public abstract Object callFunction(
    FuncExtFunction extFunction, Vector args,
      ExpressionContext exprContext) throws TransformerException;

  /**
   * Process a call to this extension namespace via an element. As a side
   * effect, the results are sent to the TransformerImpl's result tree.
   *
   * @param localPart      Element name's local part.
   * @param element        The extension element being processed.
   * @param transformer      Handle to TransformerImpl.
   * @param stylesheetTree The compiled stylesheet tree.
   * @param mode           The current mode.
   * @param sourceTree     The root of the source tree (but don't assume
   *                       it's a Document).
   * @param sourceNode     The current context node.
   * @param methodKey      A key that uniquely identifies this class and method call.
   *
   * @throws XSLProcessorException thrown if something goes wrong
   *            while running the extension handler.
   * @throws MalformedURLException if loading trouble
   * @throws FileNotFoundException if loading trouble
   * @throws IOException           if loading trouble
   * @throws TransformerException          if parsing trouble
   */
  public abstract void processElement(
    String localPart, ElemTemplateElement element, TransformerImpl transformer,
      Stylesheet stylesheetTree, Object methodKey) throws TransformerException, IOException;
}
