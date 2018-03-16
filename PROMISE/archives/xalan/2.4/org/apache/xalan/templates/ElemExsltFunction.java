package org.apache.xalan.templates;

import org.apache.xml.dtm.DTM;

import org.xml.sax.*;

import org.apache.xpath.*;
import org.apache.xpath.Expression;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XRTreeFrag;
import org.apache.xpath.objects.XRTreeFragSelectWrapper;
import org.apache.xml.utils.QName;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;

import org.apache.xalan.extensions.ExtensionsTable;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.xalan.extensions.ExtensionNamespaceSupport;
import org.apache.xalan.extensions.ExtensionHandlerExsltFunction;


/**
 * <meta name="usage" content="advanced"/>
 * Implement func:function.
 */
public class ElemExsltFunction extends ElemTemplate
{
  
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
    XPathContext xctxt = transformer.getXPathContext();
    VariableStack vars = xctxt.getVarStack();
    
    NodeList children = this.getChildNodes();
    int numparams =0;
    for (int i = 0; i < args.length; i ++)
    {
      Node child = children.item(i);
      if (children.item(i) instanceof ElemParam)
      {
        numparams++;
        ElemParam param = (ElemParam)children.item(i);
        vars.setLocalVariable (param.m_index, args[i]);
      }
    }
    if (numparams < args.length)
      throw new TransformerException ("function called with too many args");

    
    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);
    
    vars.setLocalVariable(_resultIndex, null);
    transformer.executeChildTemplates(this, true); 

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEndEvent(this);
    
    
  }
  
  /**
   * Called after everything else has been
   * recomposed, and allows the function to set remaining
   * values that may be based on some other property that
   * depends on recomposition. Also adds a slot to the variable
   * stack for the return value. The result element will place
   * its value in this slot.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    setResultIndex(cstate.addVariableName
      (new QName(Constants.S_EXSLT_COMMON_URL, "result")));
    
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
   * Add the namespace to the StylesheetRoot vector of extension namespaces. Be sure the
   * exslt:function namespace is also added.
   */
/*  public void runtimeInit(TransformerImpl transformer) throws TransformerException
  {
    String namespace = getName().getNamespace();
    ExtensionsTable etable = transformer.getExtensionsTable();
    StylesheetRoot sroot = transformer.getStylesheet();
    ExtensionHandlerExsltFunction exsltHandler =
             new ExtensionHandlerExsltFunction(namespace, sroot);
    if (!(namespace.equals(Constants.S_EXSLT_FUNCTIONS_URL)))
    {
      exsltHandler = new ExtensionHandlerExsltFunction(
                                   Constants.S_EXSLT_FUNCTIONS_URL, 
                                   sroot);
    }
  }
*/  

  private int _resultIndex;
  
  /**
   * Sets aside a position on the local variable stack index 
   * to refer to the result element return value.
   */
  void setResultIndex(int stackIndex)
  { 
      _resultIndex = stackIndex;
  }
  
  /**
   * Provides the EXSLT extension handler access to the return value.
   */
  public int getResultIndex()
  {
    return _resultIndex;
  }
  
}
