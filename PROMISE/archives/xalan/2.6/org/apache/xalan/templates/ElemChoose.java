package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;

/**
 * Implement xsl:choose.
 * <pre>
 * <!ELEMENT xsl:choose (xsl:when+, xsl:otherwise?)>
 * <!ATTLIST xsl:choose %space-att;>
 * </pre>
 * @xsl.usage advanced
 */
public class ElemChoose extends ElemTemplateElement
{

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_CHOOSE;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_CHOOSE_STRING;
  }

  /**
   * Constructor ElemChoose
   *
   */
  public ElemChoose(){}

  /**
   * Execute the xsl:choose transformation.
   *
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(TransformerImpl transformer) throws TransformerException
  {

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);

    boolean found = false;

    for (ElemTemplateElement childElem = getFirstChildElem();
            childElem != null; childElem = childElem.getNextSiblingElem())
    {
      int type = childElem.getXSLToken();

      if (Constants.ELEMNAME_WHEN == type)
      {
        found = true;

        ElemWhen when = (ElemWhen) childElem;

        XPathContext xctxt = transformer.getXPathContext();
        int sourceNode = xctxt.getCurrentNode();
        
        

        if (TransformerImpl.S_DEBUG)
        {
          XObject test = when.getTest().execute(xctxt, sourceNode, when);

          if (TransformerImpl.S_DEBUG)
            transformer.getTraceManager().fireSelectedEvent(sourceNode, when,
                    "test", when.getTest(), test);

          if (test.bool())
          {
            transformer.getTraceManager().fireTraceEvent(when);
            
            transformer.executeChildTemplates(when, true);

	        transformer.getTraceManager().fireTraceEndEvent(when); 
	                  
            return;
          }

        }
        else if (when.getTest().bool(xctxt, sourceNode, when))
        {
          transformer.executeChildTemplates(when, true);

          return;
        }
      }
      else if (Constants.ELEMNAME_OTHERWISE == type)
      {
        found = true;

        if (TransformerImpl.S_DEBUG)
          transformer.getTraceManager().fireTraceEvent(childElem);

        transformer.executeChildTemplates(childElem, true);

        if (TransformerImpl.S_DEBUG)
	      transformer.getTraceManager().fireTraceEndEvent(childElem); 
        return;
      }
    }

    if (!found)
      transformer.getMsgMgr().error(
        this, XSLTErrorResources.ER_CHOOSE_REQUIRES_WHEN);
        
    if (TransformerImpl.S_DEBUG)
	  transformer.getTraceManager().fireTraceEndEvent(this);         
  }

  /**
   * Add a child to the child list.
   *
   * @param newChild Child to add to this node's child list
   *
   * @return The child that was just added to the child list
   *
   * @throws DOMException
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    int type = ((ElemTemplateElement) newChild).getXSLToken();

    switch (type)
    {
    case Constants.ELEMNAME_WHEN :
    case Constants.ELEMNAME_OTHERWISE :

      break;
    default :
      error(XSLTErrorResources.ER_CANNOT_ADD,
            new Object[]{ newChild.getNodeName(),

    }

    return super.appendChild(newChild);
  }
  
  /**
   * Tell if this element can accept variable declarations.
   * @return true if the element can accept and process variable declarations.
   */
  public boolean canAcceptVariables()
  {
  	return false;
  }

}
