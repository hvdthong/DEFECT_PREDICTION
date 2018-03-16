package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.VariableStack;
import org.apache.xpath.objects.XObject;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:param.
 * <pre>
 * <!ELEMENT xsl:param %template;>
 * <!ATTLIST xsl:param
 *   name %qname; #REQUIRED
 *   select %expr; #IMPLIED
 * >
 * </pre>
 */
public class ElemParam extends ElemVariable
{
  int m_qnameID;

  /**
   * Constructor ElemParam
   *
   */
  public ElemParam(){}

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID of the element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_PARAMVARIABLE;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_PARAMVARIABLE_STRING;
  }

  /**
   * Copy constructor.
   *
   * @param param Element from an xsl:param
   *
   * @throws TransformerException
   */
  public ElemParam(ElemParam param) throws TransformerException
  {
    super(param);
  }

  /**
   * This function is called after everything else has been
   * recomposed, and allows the template to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    m_qnameID = sroot.getComposeState().getQNameID(m_qname);
    if(m_parentNode.getXSLToken() == Constants.ELEMNAME_TEMPLATE)
      ((ElemTemplate)m_parentNode).m_inArgsSize++;
  }
  
  /**
   * Execute a variable declaration and push it onto the variable stack.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(TransformerImpl transformer) throws TransformerException
  {
    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);
      
    VariableStack vars = transformer.getXPathContext().getVarStack();
    
    if(!vars.isLocalSet(m_index))
    {

      int sourceNode = transformer.getXPathContext().getCurrentNode();
      XObject var = getValue(transformer, sourceNode);
  
      transformer.getXPathContext().getVarStack().setLocalVariable(m_index, var);
    }
    
    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEndEvent(this);
  }
  
}
