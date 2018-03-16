package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

/**
 * TransformerFactory for xsl:template markup.
 */
class ProcessorTemplate extends ProcessorTemplateElem
{
  
  /**
   * Append the current template element to the current
   * template element, and then push it onto the current template
   * element stack.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param elem Must be a non-null reference to a {@link org.apache.xalan.templates.ElemTemplate} object.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   */
  protected void appendAndPush(
          StylesheetHandler handler, ElemTemplateElement elem)
            throws org.xml.sax.SAXException
  {

    super.appendAndPush(handler, elem);
    elem.setDOMBackPointer(handler.getOriginatingNode());
    handler.getStylesheet().setTemplate((ElemTemplate) elem);
  }
}
