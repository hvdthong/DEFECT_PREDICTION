package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

/**
 * Handles the EXSLT result element within an EXSLT function element.
 */
public class ElemExsltFuncResult extends ElemVariable
{
    static final long serialVersionUID = -3478311949388304563L;
    /*
     * To keep the binary compatibility put those three private global 
     * variables back, although they are never used in this verison
     */
    private boolean m_isResultSet = false;
  
    private XObject m_result = null;
  
    private int m_callerFrameSize = 0;
 
  /**
   * Generate the EXSLT function return value, and assign it to the variable
   * index slot assigned for it in ElemExsltFunction compose().
   * 
   */
  public void execute(TransformerImpl transformer) throws TransformerException
  {    
    XPathContext context = transformer.getXPathContext();

    if (transformer.getDebug())
      transformer.getTraceManager().fireTraceEvent(this);
    
    if (transformer.currentFuncResultSeen()) {
        throw new TransformerException("An EXSLT function cannot set more than one result!");
    }

    int sourceNode = context.getCurrentNode();

    XObject var = getValue(transformer, sourceNode);
    transformer.popCurrentFuncResult();
    transformer.pushCurrentFuncResult(var);

    if (transformer.getDebug())
      transformer.getTraceManager().fireTraceEndEvent(this);    
  }

  /**
   * Get an integer representation of the element type.
   *
   * @return An integer representation of the element, defined in the
   *     Constants class.
   * @see org.apache.xalan.templates.Constants
   */
  public int getXSLToken()
  {
    return Constants.EXSLT_ELEMNAME_FUNCRESULT;
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
    return Constants.EXSLT_ELEMNAME_FUNCRESULT_STRING;
  }
}
