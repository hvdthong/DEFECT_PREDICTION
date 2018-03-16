package org.apache.xalan.extensions;

import java.util.Vector;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.apache.xalan.transformer.TransformerImpl;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemExsltFunction;
import org.apache.xalan.templates.ElemExsltFuncResult;
import org.apache.xalan.templates.Constants;

import org.apache.xml.utils.QName;

import org.apache.xpath.XPathContext;
import org.apache.xpath.VariableStack;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import javax.xml.transform.TransformerException;

/**
 * Execute EXSLT functions, determine the availability of EXSLT functions, and the
 * availability of an EXSLT result element.
 */
public class ExtensionHandlerExsltFunction extends ExtensionHandler
{
  private String m_namespace;
  private StylesheetRoot m_stylesheet;
  private static final QName RESULTQNAME = 
                  new QName(Constants.S_EXSLT_FUNCTIONS_URL,
                            Constants.EXSLT_ELEMNAME_FUNCRESULT_STRING);
  /**
   * Constructor called from ElemExsltFunction runtimeInit().
   */  
  public ExtensionHandlerExsltFunction(String ns, StylesheetRoot stylesheet)
  {
    m_namespace = ns;
    m_stylesheet = stylesheet;
  }
  
  /**
   * Required by ExtensionHandler (an abstract method). No-op.
   */
  public void processElement(
    String localPart, ElemTemplateElement element, TransformerImpl transformer,
    Stylesheet stylesheetTree, Object methodKey) throws TransformerException, IOException
  {}
  
  /**
   * Get the ElemExsltFunction element associated with the 
   * function.
   * 
   * @param funcName Local name of the function.
   * @return the ElemExsltFunction element associated with
   * the function, null if none exists.
   */
  public ElemExsltFunction getFunction(String funcName)
  {
    QName qname = new QName(m_namespace, funcName);
    ElemTemplate templ = m_stylesheet.getTemplateComposed(qname);
    if (templ != null && templ instanceof ElemExsltFunction)
      return (ElemExsltFunction) templ;
    else
      return null;    
  }
  
  
  /**
   * Does the EXSLT function exist?
   * 
   * @param funcName Local name of the function.
   * @return true if the function exists.
   */  
  public boolean isFunctionAvailable(String funcName)
  {
    return getFunction(funcName)!= null;
  }
    
   /** If an element-available() call applies to an EXSLT result element within 
   * an EXSLT function element, return true.
   *
   * Note: The EXSLT function element is a template-level element, and 
   * element-available() returns false for it.
   * 
   * @param Local name of the function.
   * @return true if the function is available.
   */
  public boolean isElementAvailable(String elemName)
  {
    if (!(new QName(m_namespace, elemName).equals(RESULTQNAME)))
    {
      return false;
    }
    else
    {
      ElemTemplateElement elem = m_stylesheet.getFirstChildElem();
      while (elem != null && elem != m_stylesheet)
      {
        if (elem instanceof ElemExsltFuncResult && ancestorIsFunction(elem))
          return true;
        ElemTemplateElement  nextElem = elem.getFirstChildElem();
        if (nextElem == null)
          nextElem = elem.getNextSiblingElem();
        if (nextElem == null)
          nextElem = elem.getParentElem();
        elem = nextElem;
      }
    }
    return false;
  }

  /**
   * Determine whether the func:result element is within a func:function element. 
   * If not, it is illegal.
   */
  private boolean ancestorIsFunction(ElemTemplateElement child)
  {
    while (child.getParentElem() != null 
           && !(child.getParentElem() instanceof StylesheetRoot))
    {
      if (child.getParentElem() instanceof ElemExsltFunction)
        return true;
      child = child.getParentElem();      
    }
    return false;
  }

  /**
   * Execute the EXSLT function and return the result value.
   * 
   * @param funcName Name of the EXSLT function.
   * @param args     The arguments of the function call.
   * @param methodKey Not used.
   * @param exprContext Used to get the XPathContext.
   * @return the return value of the function evaluation.
   * @throws TransformerException
   */
  public Object callFunction(
      String funcName, Vector args, Object methodKey,
      ExpressionContext exprContext) throws TransformerException
  {
    XObject[] methodArgs;
    methodArgs = new XObject[args.size()];
    try
    {
      for (int i = 0; i < methodArgs.length; i++)
      {
        methodArgs[i] =  XObject.create(args.elementAt(i));
      }
      ElemExsltFunction elemFunc = getFunction(funcName);
      XPathContext context = exprContext.getXPathContext();
      TransformerImpl transformer = (TransformerImpl)context.getOwnerObject();
      elemFunc.execute(transformer, methodArgs);
      
      VariableStack varStack = context.getVarStack();
      
      int resultIndex = elemFunc.getResultIndex();   
      if (varStack.isLocalSet(resultIndex))
        val = varStack.getLocalVariable(context, resultIndex);
      return val;
    }
    catch (Exception e)
    {
      throw new TransformerException(e);
    }
  }
  
}
