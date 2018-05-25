package org.apache.xalan.processor;

import java.util.Vector;

import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.WhiteSpaceInfo;
import org.apache.xpath.XPath;

import org.xml.sax.Attributes;

/**
 * TransformerFactory for xsl:strip-space markup.
 * <pre>
 * <!ELEMENT xsl:strip-space EMPTY>
 * <!ATTLIST xsl:strip-space elements CDATA #REQUIRED>
 * </pre>
 */
class ProcessorStripSpace extends ProcessorPreserveSpace
{

  /**
   * Receive notification of the start of an strip-space element.
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
    Stylesheet thisSheet = handler.getStylesheet();
	WhitespaceInfoPaths paths = new WhitespaceInfoPaths(thisSheet);
    setPropertiesFromAttributes(handler, rawName, attributes, paths);

    Vector xpaths = paths.getElements();

    for (int i = 0; i < xpaths.size(); i++)
    {
      WhiteSpaceInfo wsi = new WhiteSpaceInfo((XPath) xpaths.elementAt(i), true, thisSheet);
      wsi.setUid(handler.nextUid());

      thisSheet.setStripSpaces(wsi);
    }
    paths.clearElements();

  }
}