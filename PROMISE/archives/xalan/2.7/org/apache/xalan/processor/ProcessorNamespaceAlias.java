package org.apache.xalan.processor;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.templates.NamespaceAlias;
import org.xml.sax.Attributes;

/**
 * TransformerFactory for xsl:namespace-alias markup.
 * A stylesheet can use the xsl:namespace-alias element to
 * declare that one namespace URI is an alias for another namespace URI.
 * <pre>
 * <!ELEMENT xsl:namespace-alias EMPTY>
 * <!ATTLIST xsl:namespace-alias
 *   stylesheet-prefix CDATA #REQUIRED
 *   result-prefix CDATA #REQUIRED
 * >
 * </pre>
 */
class ProcessorNamespaceAlias extends XSLTElementProcessor
{
    static final long serialVersionUID = -6309867839007018964L;

  /**
   * Receive notification of the start of an xsl:namespace-alias element.
   *
   * @param handler The calling StylesheetHandler/TemplatesBuilder.
   * @param uri The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param localName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param rawName The raw XML 1.0 name (with prefix), or the
   *        empty string if raw names are not available.
   * @param attributes The attributes attached to the element.  If
   *        there are no attributes, it shall be an empty
   *        Attributes object.
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException
  {
    final String resultNS;
    NamespaceAlias na = new NamespaceAlias(handler.nextUid());

    setPropertiesFromAttributes(handler, rawName, attributes, na);
    String prefix = na.getStylesheetPrefix();
    if(prefix.equals("#default"))
    {
      prefix = "";
      na.setStylesheetPrefix(prefix);
    }
    String stylesheetNS = handler.getNamespaceForPrefix(prefix);
    na.setStylesheetNamespace(stylesheetNS);
    prefix = na.getResultPrefix();
    if(prefix.equals("#default"))
    {
      prefix = "";
      na.setResultPrefix(prefix);
      resultNS = handler.getNamespaceForPrefix(prefix);
      if(null == resultNS)
        handler.error(XSLTErrorResources.ER_INVALID_NAMESPACE_URI_VALUE_FOR_RESULT_PREFIX_FOR_DEFAULT, null, null);
    }
    else
    {
        resultNS = handler.getNamespaceForPrefix(prefix);
        if(null == resultNS)
         handler.error(XSLTErrorResources.ER_INVALID_NAMESPACE_URI_VALUE_FOR_RESULT_PREFIX, new Object[] {prefix}, null);
    }
   
    na.setResultNamespace(resultNS);
    handler.getStylesheet().setNamespaceAlias(na);
    handler.getStylesheet().appendChild(na);
  }
}
