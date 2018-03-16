package org.apache.xalan.processor;

import java.lang.StringBuffer;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;

import org.apache.xalan.templates.ElemText;
import org.apache.xalan.templates.ElemTextLiteral;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Constants;
import org.apache.xml.utils.XMLCharacterRecognizer;

/**
 * This class processes character events for a XSLT template element.
 */
public class ProcessorCharacters extends XSLTElementProcessor
{

  /**
   * Receive notification of the start of the non-text event.  This
   * is sent to the current processor when any non-text event occurs.
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   */
  public void startNonText(StylesheetHandler handler) throws org.xml.sax.SAXException
  {
    if (this == handler.getCurrentProcessor())
    {
      handler.popProcessor();
    }

    int nChars = m_accumulator.length();

    if ((nChars > 0)
            && ((null != m_xslTextElement)
                ||!XMLCharacterRecognizer.isWhiteSpace(m_accumulator)) 
                || handler.isSpacePreserve())
    {
      ElemTextLiteral elem = new ElemTextLiteral();

      elem.setDOMBackPointer(m_firstBackPointer);
      elem.setLocaterInfo(handler.getLocator());
      try
      {
        elem.setPrefixes(handler.getNamespaceSupport());
      }
      catch(TransformerException te)
      {
        throw new org.xml.sax.SAXException(te);
      }

      boolean doe = (null != m_xslTextElement)
                    ? m_xslTextElement.getDisableOutputEscaping() : false;

      elem.setDisableOutputEscaping(doe);
      elem.setPreserveSpace(true);

      char[] chars = new char[nChars];

      m_accumulator.getChars(0, nChars, chars, 0);
      elem.setChars(chars);

      ElemTemplateElement parent = handler.getElemTemplateElement();

      parent.appendChild(elem);
    }

    m_accumulator.setLength(0);
    m_firstBackPointer = null;
  }
  
  protected Node m_firstBackPointer = null;

  /**
   * Receive notification of character data inside an element.
   *
   *
   * @param handler non-null reference to current StylesheetHandler that is constructing the Templates.
   * @param ch The characters.
   * @param start The start position in the character array.
   * @param length The number of characters to use from the
   *               character array.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#characters
   */
  public void characters(
          StylesheetHandler handler, char ch[], int start, int length)
            throws org.xml.sax.SAXException
  {

    m_accumulator.append(ch, start, length);
    
    if(null == m_firstBackPointer)
      m_firstBackPointer = handler.getOriginatingNode();

    if (this != handler.getCurrentProcessor())
      handler.pushProcessor(this);
  }

  /**
   * Receive notification of the end of an element.
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
   * @param atts The attributes attached to the element.  If
   *        there are no attributes, it shall be an empty
   *        Attributes object.
   * @see org.apache.xalan.processor.StylesheetHandler#startElement
   * @see org.apache.xalan.processor.StylesheetHandler#endElement
   * @see org.xml.sax.ContentHandler#startElement
   * @see org.xml.sax.ContentHandler#endElement
   * @see org.xml.sax.Attributes
   */
  public void endElement(
          StylesheetHandler handler, String uri, String localName, String rawName)
            throws org.xml.sax.SAXException
  {

    startNonText(handler);
    handler.getCurrentProcessor().endElement(handler, uri, localName,
                                             rawName);
    handler.popProcessor();
  }

  /**
   * Accumulate characters, until a non-whitespace event has
   * occured.
   */
  private StringBuffer m_accumulator = new StringBuffer();

  /**
   * The xsl:text processor will call this to set a
   * preserve space state.
   */
  private ElemText m_xslTextElement;

  /**
   * Set the current setXslTextElement. The xsl:text 
   * processor will call this to set a preserve space state.
   *
   * @param xslTextElement The current xslTextElement that 
   *                       is preserving state, or null.
   */
  void setXslTextElement(ElemText xslTextElement)
  {
    m_xslTextElement = xslTextElement;
  }
}
