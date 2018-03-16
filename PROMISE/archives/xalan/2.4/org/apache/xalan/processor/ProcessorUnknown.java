package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemLiteralResult;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.templates.ElemExtensionCall;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.Constants;
import org.apache.xpath.XPath;
import org.apache.xalan.templates.StylesheetRoot;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

/**
 * <meta name="usage" content="internal"/>
 * This class processes an unknown template element, and ignores 
 * the startElement and endElement events.  It is used both 
 * for unknown top-level elements, and for elements in the 
 * xslt namespace when the version is higher than the version 
 * of XSLT that we are set up to process.
 */
public class ProcessorUnknown extends ProcessorTemplateElem
{

  /**
   * Receive notification of the start of an element.
   *
   * @param name The element type name.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param uri The Namespace URI, or an empty string.
   * @param localName The local name (without prefix), or empty string if not namespace processing.
   * @param rawName The qualified name (with prefix).
   * @param attributes The specified or defaulted attributes.
   */
  public void startElement(
          StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes)
            throws org.xml.sax.SAXException{}

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
            throws org.xml.sax.SAXException{}
}
