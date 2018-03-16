package org.apache.xalan.processor;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemText;

/**
 * Process xsl:text.
 */
public class ProcessorText extends ProcessorTemplateElem
{

  /**
   * Append the current template element to the current
   * template element, and then push it onto the current template
   * element stack.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param elem non-null reference to a {@link org.apache.xalan.templates.ElemText}.
   *
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   */
  protected void appendAndPush(
          StylesheetHandler handler, ElemTemplateElement elem)
            throws org.xml.sax.SAXException
  {

    ProcessorCharacters charProcessor =
      (ProcessorCharacters) handler.getProcessorFor(null, "text()", "text");

    charProcessor.setXslTextElement((ElemText) elem);

    ElemTemplateElement parent = handler.getElemTemplateElement();

    parent.appendChild(elem);
    elem.setDOMBackPointer(handler.getOriginatingNode());
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

    ProcessorCharacters charProcessor 
      = (ProcessorCharacters) handler.getProcessorFor(null, "text()", "text");

    charProcessor.setXslTextElement(null);

  }
}
