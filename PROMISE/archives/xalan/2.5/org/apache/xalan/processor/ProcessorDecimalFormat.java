package org.apache.xalan.processor;

import org.apache.xalan.templates.DecimalFormatProperties;
import org.xml.sax.Attributes;

/**
 * <meta name="usage" content="internal"/>
 * Process xsl:decimal-format by creating a DecimalFormatProperties 
 * object and passing it to the stylesheet.
 * 
 * @see org.apache.xalan.templates.Stylesheet#setDecimalFormat
 * @see org.apache.xalan.templates.DecimalFormatProperties
 */
class ProcessorDecimalFormat extends XSLTElementProcessor
{

  /**
   * Receive notification of the start of an element.
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
   * @see org.apache.xalan.processor.StylesheetHandler#startElement
   * @see org.apache.xalan.processor.StylesheetHandler#endElement
   * @see org.xml.sax.ContentHandler#startElement
   * @see org.xml.sax.ContentHandler#endElement
   * @see org.xml.sax.Attributes
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException
  {

    DecimalFormatProperties dfp = new DecimalFormatProperties(handler.nextUid());

    setPropertiesFromAttributes(handler, rawName, attributes, dfp);
    handler.getStylesheet().setDecimalFormat(dfp);
    
    handler.getStylesheet().appendChild(dfp);
  }
}
