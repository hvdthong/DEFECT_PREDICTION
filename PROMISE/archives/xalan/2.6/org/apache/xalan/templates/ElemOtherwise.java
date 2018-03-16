package org.apache.xalan.templates;


/**
 * Implement xsl:otherwise.
 * <pre>
 * <!ELEMENT xsl:otherwise %template;>
 * <!ATTLIST xsl:otherwise %space-att;>
 * </pre>
 * @xsl.usage advanced
 */
public class ElemOtherwise extends ElemTemplateElement
{

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_OTHERWISE;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_OTHERWISE_STRING;
  }
}
