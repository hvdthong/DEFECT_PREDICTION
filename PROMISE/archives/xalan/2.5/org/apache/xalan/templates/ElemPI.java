package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathContext;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:processing-instruction.
 * <pre>
 * <!ELEMENT xsl:processing-instruction %char-template;>
 * <!ATTLIST xsl:processing-instruction
 *   name %avt; #REQUIRED
 *   %space-att;
 * >
 * </pre>
 */
public class ElemPI extends ElemTemplateElement
{

  /**
   * The xsl:processing-instruction element has a required name
   * attribute that specifies the name of the processing instruction node.
   * The value of the name attribute is interpreted as an
   * attribute value template.
   * @serial
   */
  private AVT m_name_atv = null;

  /**
   * Set the "name" attribute.
   * DJD
   *
   * @param v Value for the name attribute
   */
  public void setName(AVT v)
  {
    m_name_atv = v;
  }

  /**
   * Get the "name" attribute.
   * DJD
   *
   * @return The value of the "name" attribute 
   */
  public AVT getName()
  {
    return m_name_atv;
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
    java.util.Vector vnames = sroot.getComposeState().getVariableNames();
    if(null != m_name_atv)
      m_name_atv.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
  }



  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for the element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_PI;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_PI_STRING;
  }

  /**
   * Create a processing instruction in the result tree.
   * The content of the xsl:processing-instruction element is a
   * template for the string-value of the processing instruction node.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);

    XPathContext xctxt = transformer.getXPathContext();
    int sourceNode = xctxt.getCurrentNode();
    
    String piName = m_name_atv == null ? null : m_name_atv.evaluate(xctxt, sourceNode, this);
    
    if (piName == null) return;

    if (piName.equalsIgnoreCase("xml"))
    {
     	transformer.getMsgMgr().warn(
        this, XSLTErrorResources.WG_PROCESSINGINSTRUCTION_NAME_CANT_BE_XML,
              new Object[]{ Constants.ATTRNAME_NAME, piName });
		return;
    }
    
    else if ((!m_name_atv.isSimple()) && (!isValidNCName(piName)))
    {
     	transformer.getMsgMgr().warn(
        this, XSLTErrorResources.WG_PROCESSINGINSTRUCTION_NOTVALID_NCNAME,
              new Object[]{ Constants.ATTRNAME_NAME, piName });
		return;    	
    }

    String data = transformer.transformToString(this);

    try
    {
      transformer.getResultTreeHandler().processingInstruction(piName, data);
    }
    catch(org.xml.sax.SAXException se)
    {
      throw new TransformerException(se);
    }
    
    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEndEvent(this);
  }

  /**
   * Add a child to the child list.
   *
   * @param newChild Child to add to child list
   *
   * @return The child just added to the child list
   *
   * @throws DOMException
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    int type = ((ElemTemplateElement) newChild).getXSLToken();

    switch (type)
    {

    case Constants.ELEMNAME_TEXTLITERALRESULT :
    case Constants.ELEMNAME_APPLY_TEMPLATES :
    case Constants.ELEMNAME_APPLY_IMPORTS :
    case Constants.ELEMNAME_CALLTEMPLATE :
    case Constants.ELEMNAME_FOREACH :
    case Constants.ELEMNAME_VALUEOF :
    case Constants.ELEMNAME_COPY_OF :
    case Constants.ELEMNAME_NUMBER :
    case Constants.ELEMNAME_CHOOSE :
    case Constants.ELEMNAME_IF :
    case Constants.ELEMNAME_TEXT :
    case Constants.ELEMNAME_COPY :
    case Constants.ELEMNAME_VARIABLE :
    case Constants.ELEMNAME_MESSAGE :

      break;
    default :
      error(XSLTErrorResources.ER_CANNOT_ADD,
            new Object[]{ newChild.getNodeName(),

    }

    return super.appendChild(newChild);
  }
}
