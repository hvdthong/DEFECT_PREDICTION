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
 
  /**
   * Generate the EXSLT function return value, and assign it to the variable
   * index slot assigned for it in ElemExsltFunction compose().
   * 
   */
  public void execute(TransformerImpl transformer) throws TransformerException
  {    
    XPathContext context = transformer.getXPathContext();
    ElemExsltFunction owner = getOwnerFunction();
    if (owner != null)
    {
      if (owner.isResultSet())
        throw new TransformerException("An EXSLT function cannot set more than one result!");
      
      int sourceNode = context.getCurrentNode();
      XObject var = getValue(transformer, sourceNode);
      owner.setResult(var);
    }    
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
   * @see org.apache.xalan.templates.Constants.
   * @return The node name
   * 
   */
   public String getNodeName()
  {
    return Constants.EXSLT_ELEMNAME_FUNCRESULT_STRING;
  }
  
  /**
   * Get the ElemExsltFunction that contains the ElemResult so we can set an ElemExsltFunction variable
   * to the local variable stack index to the return value.
   */
  public ElemExsltFunction getOwnerFunction()
  {
  	ElemTemplateElement elem = this;
  	while((elem != null) && !(elem instanceof ElemExsltFunction))
  	{
    	elem = elem.getParentElem();
  	}
  	return (ElemExsltFunction)elem;
  }
  
}
