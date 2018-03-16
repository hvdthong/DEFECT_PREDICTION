package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.extensions.ExtensionNamespaceSupport;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * <meta name="usage" content="advanced"/>
 * Implement func:function.
 */
public class ElemExsltFunction extends ElemTemplate
{
  
  private boolean m_isResultSet = false;
  
  private XObject m_result;
  
  private int m_callerFrameSize = 0;
  
  /**
   * Get an integer representation of the element type.
   *
   * @return An integer representation of the element, defined in the
   *     Constants class.
   * @see org.apache.xalan.templates.Constants
   */
  public int getXSLToken()
  {
    return Constants.EXSLT_ELEMNAME_FUNCTION;
  }

   /**
   * Return the node name, defined in the
   *     Constants class.
   * @see org.apache.xalan.templates.Constants.
   * @return The node name
   * 
   */ 
  public String getNodeName()
  {
    return Constants.EXSLT_ELEMNAME_FUNCTION_STRING;
  }
  
  public void execute(TransformerImpl transformer, XObject[] args)
          throws TransformerException
  {
    m_isResultSet = false;
    m_result = null;
    
    XPathContext xctxt = transformer.getXPathContext();
    VariableStack vars = xctxt.getVarStack();
    
    int oldStackFrame = vars.getStackFrame();
    vars.setStackFrame(m_callerFrameSize + oldStackFrame);
    
    NodeList children = this.getChildNodes();
    int numparams =0;
    for (int i = 0; i < args.length; i ++)
    {
      Node child = children.item(i);
      if (children.item(i) instanceof ElemParam)
      {
        numparams++;
        ElemParam param = (ElemParam)children.item(i);
        vars.setLocalVariable (param.getIndex(), args[i]);
      }
    }
    if (numparams < args.length)
      throw new TransformerException ("function called with too many args");

    
    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);
    
    transformer.executeChildTemplates(this, true);
    
    vars.setStackFrame(oldStackFrame);
    m_callerFrameSize = 0;

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEndEvent(this);
    
    
  }
  
  /**
   * Called after everything else has been
   * recomposed, and allows the function to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    
    String namespace = getName().getNamespace();
    String handlerClass = "org.apache.xalan.extensions.ExtensionHandlerExsltFunction";    
    Object[] args ={namespace, sroot};
    ExtensionNamespaceSupport extNsSpt = 
                         new ExtensionNamespaceSupport(namespace, handlerClass, args);
    sroot.getExtensionNamespacesManager().registerExtension(extNsSpt);
    if (!(namespace.equals(Constants.S_EXSLT_FUNCTIONS_URL)))
    {
      namespace = Constants.S_EXSLT_FUNCTIONS_URL;
      args = new Object[]{namespace, sroot};
      extNsSpt = new ExtensionNamespaceSupport(namespace, handlerClass, args);
      sroot.getExtensionNamespacesManager().registerExtension(extNsSpt);
    }
  }
  
  /**
   * Return the result of this EXSLT function
   *
   * @return The result of this EXSLT function
   */
  public XObject getResult()
  {
    return m_result;
  }
  
  /**
   * Set the return result of this EXSLT function
   *
   * @param result The return result
   */
  public void setResult(XObject result)
  {
    m_isResultSet = true;
    m_result = result;
  }
  
  /**
   * Return true if the result has been set
   *
   * @return true if the result has been set
   */
  public boolean isResultSet()
  {
    return m_isResultSet;
  }
  
  /**
   * Clear the return result of this EXSLT function
   */
  public void clearResult()
  {
    m_isResultSet = false;
    m_result = null;    
  }
  
  /**
   * Set the frame size of the current caller for use in the variable stack.
   *
   * @param callerFrameSize The frame size of the caller
   */
  public void setCallerFrameSize(int callerFrameSize)
  {
    m_callerFrameSize = callerFrameSize;
  }
}
