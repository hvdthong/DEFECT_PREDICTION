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
 * Implement func:function.
 * @xsl.usage advanced
 */
public class ElemExsltFunction extends ElemTemplate
{
    static final long serialVersionUID = 272154954793534771L;
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
   * @see org.apache.xalan.templates.Constants
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
    
    int thisFrame = vars.getStackFrame();
    int nextFrame = vars.link(m_frameSize);

    if (m_inArgsSize < args.length) {
      throw new TransformerException ("function called with too many args");
    }
    
    if (m_inArgsSize > 0) {
      vars.clearLocalSlots(0, m_inArgsSize);

      if (args.length > 0) {
        vars.setStackFrame(thisFrame);
        NodeList children = this.getChildNodes();
        
        for (int i = 0; i < args.length; i ++) {
          Node child = children.item(i);
          if (children.item(i) instanceof ElemParam) {
            ElemParam param = (ElemParam)children.item(i);
            vars.setLocalVariable(param.getIndex(), args[i], nextFrame);
          }
        }
        
        vars.setStackFrame(nextFrame);
      }
    }

    
    if (transformer.getDebug())
      transformer.getTraceManager().fireTraceEvent(this);
    
    vars.setStackFrame(nextFrame);
    transformer.executeChildTemplates(this, true);
    
    vars.unlink(thisFrame);

    if (transformer.getDebug())
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
    String handlerClass = sroot.getExtensionHandlerClass();
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
}
