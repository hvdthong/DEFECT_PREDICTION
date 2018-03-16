package org.apache.xalan.processor;

import javax.xml.transform.TransformerException;

import org.apache.xalan.templates.ElemAttributeSet;
import org.apache.xalan.templates.ElemTemplateElement;

import org.xml.sax.Attributes;

/**
 * This class processes parse events for an xsl:attribute-set.
 */
class ProcessorAttributeSet extends XSLTElementProcessor
{
    static final long serialVersionUID = -6473739251316787552L;

  /**
   * Receive notification of the start of an xsl:attribute-set element.
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
   * 
   * @see org.apache.xalan.processor.StylesheetHandler#startElement
   * @see org.xml.sax.ContentHandler#startElement
   * @see org.xml.sax.ContentHandler#endElement
   * @see org.xml.sax.Attributes
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException
  {

    ElemAttributeSet eat = new ElemAttributeSet();

    eat.setLocaterInfo(handler.getLocator());
    try
    {
      eat.setPrefixes(handler.getNamespaceSupport());
    }
    catch(TransformerException te)
    {
      throw new org.xml.sax.SAXException(te);
    }

    eat.setDOMBackPointer(handler.getOriginatingNode());
    setPropertiesFromAttributes(handler, rawName, attributes, eat);
    handler.getStylesheet().setAttributeSet(eat);

    ElemTemplateElement parent = handler.getElemTemplateElement();

    parent.appendChild(eat);
    handler.pushElemTemplateElement(eat);
  }

  /**
   * Receive notification of the end of an element.
   *
   * @param name The element type name.
   * @param attributes The specified or defaulted attributes.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param uri The Namespace URI, or an empty string.
   * @param localName The local name (without prefix), or empty string if not namespace processing.
   * @param rawName The qualified name (with prefix).
   */
  public void endElement(
          StylesheetHandler handler, String uri, String localName, String rawName)
            throws org.xml.sax.SAXException
  {
    handler.popElemTemplateElement();
  }
}
