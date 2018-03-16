package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTM;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:apply-imports.
 * <pre>
 * <!ELEMENT xsl:apply-imports EMPTY>
 * </pre>
 */
public class ElemApplyImport extends ElemTemplateElement
{

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return Token ID for xsl:apply-imports element types
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_APPLY_IMPORTS;
  }

  /**
   * Return the node name.
   *
   * @return Element name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_APPLY_IMPORTS_STRING;
  }

  /**
   * Execute the xsl:apply-imports transformation.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

    if (transformer.currentTemplateRuleIsNull())
    {
      transformer.getMsgMgr().error(this,
    }

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);

    int sourceNode = transformer.getXPathContext().getCurrentNode();
    if (DTM.NULL != sourceNode)
    {

      transformer.applyTemplateToNode(this, null, sourceNode);
    }
    {
      transformer.getMsgMgr().error(this,
    }
    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEndEvent(this);
  }

  /**
   * Add a child to the child list.
   * <!ELEMENT xsl:apply-imports EMPTY>
   *
   * @param newChild New element to append to this element's children list
   *
   * @return null, xsl:apply-Imports cannot have children 
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    error(XSLTErrorResources.ER_CANNOT_ADD,
          new Object[]{ newChild.getNodeName(),

    return null;
  }
}
