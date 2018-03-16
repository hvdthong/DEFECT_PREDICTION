package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemParam;
import org.apache.xalan.templates.ElemTemplateElement;

/**
 * This class processes parse events for an xsl:param element.
 */
class ProcessorGlobalParamDecl extends ProcessorTemplateElem
{
    static final long serialVersionUID = 1900450872353587350L;

  /**
   * Append the current template element to the current
   * template element, and then push it onto the current template
   * element stack.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param elem The non-null reference to the ElemParam element.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   */
  protected void appendAndPush(
          StylesheetHandler handler, ElemTemplateElement elem)
            throws org.xml.sax.SAXException
  {

    handler.pushElemTemplateElement(elem);
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

    ElemParam v = (ElemParam) handler.getElemTemplateElement();

    handler.getStylesheet().appendChild(v);
    handler.getStylesheet().setParam(v);
    super.endElement(handler, uri, localName, rawName);
  }
}
