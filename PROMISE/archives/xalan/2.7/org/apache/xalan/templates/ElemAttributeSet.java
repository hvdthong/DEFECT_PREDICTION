package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.QName;

/**
 * Implement xsl:attribute-set.
 * <pre>
 * &amp;!ELEMENT xsl:attribute-set (xsl:attribute)*>
 * &amp;!ATTLIST xsl:attribute-set
 *   name %qname; #REQUIRED
 *   use-attribute-sets %qnames; #IMPLIED
 * &amp;
 * </pre>
 * @xsl.usage advanced
 */
public class ElemAttributeSet extends ElemUse
{
    static final long serialVersionUID = -426740318278164496L;

  /**
   * The name attribute specifies the name of the attribute set.
   * @serial
   */
  public QName m_qname = null;

  /**
   * Set the "name" attribute.
   * The name attribute specifies the name of the attribute set.
   *
   * @param name Name attribute to set
   */
  public void setName(QName name)
  {
    m_qname = name;
  }

  /**
   * Get the "name" attribute.
   * The name attribute specifies the name of the attribute set.
   *
   * @return The name attribute of the attribute set
   */
  public QName getName()
  {
    return m_qname;
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return Token ID of the element 
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_DEFINEATTRIBUTESET;
  }

  /**
   * Return the node name.
   *
   * @return The name of this element
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_ATTRIBUTESET_STRING;
  }

  /**
   * Apply a set of attributes to the element.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

    if (transformer.getDebug())
	  transformer.getTraceManager().fireTraceEvent(this);

    if (transformer.isRecursiveAttrSet(this))
    {
      throw new TransformerException(
        XSLMessages.createMessage(
          XSLTErrorResources.ER_XSLATTRSET_USED_ITSELF,
    }

    transformer.pushElemAttributeSet(this);
    super.execute(transformer);

    ElemAttribute attr = (ElemAttribute) getFirstChildElem();

    while (null != attr)
    {
      attr.execute(transformer);

      attr = (ElemAttribute) attr.getNextSiblingElem();
    }

    transformer.popElemAttributeSet();
   
    if (transformer.getDebug())
	  transformer.getTraceManager().fireTraceEndEvent(this);

  }

  /**
   * Add a child to the child list.
   * <!ELEMENT xsl:attribute-set (xsl:attribute)*>
   * <!ATTLIST xsl:attribute-set
   *   name %qname; #REQUIRED
   *   use-attribute-sets %qnames; #IMPLIED
   * >
   *
   * @param newChild Child to be added to this node's list of children
   *
   * @return The child that was just added to the list of children
   *
   * @throws DOMException
   */
  public ElemTemplateElement appendChildElem(ElemTemplateElement newChild)
  {

    int type = ((ElemTemplateElement) newChild).getXSLToken();

    switch (type)
    {
    case Constants.ELEMNAME_ATTRIBUTE :
      break;
    default :
      error(XSLTErrorResources.ER_CANNOT_ADD,
            new Object[]{ newChild.getNodeName(),

    }

    return super.appendChild(newChild);
  }

  /**
   * This function is called during recomposition to
   * control how this element is composed.
   * @param root The root stylesheet for this transformation.
   */
  public void recompose(StylesheetRoot root)
  {
    root.recomposeAttributeSets(this);
  }

}
