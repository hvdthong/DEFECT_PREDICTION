package org.apache.xalan.templates;

import org.apache.xalan.res.XSLTErrorResources;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:template.
 * This primarily acts as a marker on the element
 * stack to signal that whitespace should be preserved.
 * <pre>
 * <!ELEMENT xsl:text (#PCDATA)>
 * <!ATTLIST xsl:text
 *   disable-output-escaping (yes|no) "no"
 * >
 * </pre>
 */
public class ElemText extends ElemTemplateElement
{

  /**
   * Tells if this element should disable escaping.
   * @serial
   */
  private boolean m_disableOutputEscaping = false;

  /**
   * Set the "disable-output-escaping" attribute.
   * Normally, the xml output method escapes & and < (and
   * possibly other characters) when outputting text nodes.
   * This ensures that the output is well-formed XML. However,
   * it is sometimes convenient to be able to produce output
   * that is almost, but not quite well-formed XML; for
   * example, the output may include ill-formed sections
   * which are intended to be transformed into well-formed
   * XML by a subsequent non-XML aware process. For this reason,
   * XSLT provides a mechanism for disabling output escaping.
   * An xsl:value-of or xsl:text element may have a
   * disable-output-escaping attribute; the allowed values
   * are yes or no; the default is no; if the value is yes,
   * then a text node generated by instantiating the xsl:value-of
   * or xsl:text element should be output without any escaping.
   *
   * @param v Boolean flag indicating whether this element should disable escaping
   */
  public void setDisableOutputEscaping(boolean v)
  {
    m_disableOutputEscaping = v;
  }

  /**
   * Get the "disable-output-escaping" attribute.
   * Normally, the xml output method escapes & and < (and
   * possibly other characters) when outputting text nodes.
   * This ensures that the output is well-formed XML. However,
   * it is sometimes convenient to be able to produce output
   * that is almost, but not quite well-formed XML; for
   * example, the output may include ill-formed sections
   * which are intended to be transformed into well-formed
   * XML by a subsequent non-XML aware process. For this reason,
   * XSLT provides a mechanism for disabling output escaping.
   * An xsl:value-of or xsl:text element may have a
   * disable-output-escaping attribute; the allowed values
   * are yes or no; the default is no; if the value is yes,
   * then a text node generated by instantiating the xsl:value-of
   * or xsl:text element should be output without any escaping.
   *
   * @return Boolean flag indicating whether this element should disable escaping
   */
  public boolean getDisableOutputEscaping()
  {
    return m_disableOutputEscaping;
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
    return Constants.ELEMNAME_TEXT;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_TEXT_STRING;
  }

  /**
   * Add a child to the child list.
   *
   * @param newChild Child to add to children list
   *
   * @return Child added to children list
   *
   * @throws DOMException
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    int type = ((ElemTemplateElement) newChild).getXSLToken();

    switch (type)
    {
    case Constants.ELEMNAME_TEXTLITERALRESULT :
      break;
    default :
      error(XSLTErrorResources.ER_CANNOT_ADD,
            new Object[]{ newChild.getNodeName(),

    }

    return super.appendChild(newChild);
  }
}
