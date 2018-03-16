package org.apache.xpath;

/**
 * Derived from XPathException in order that XPath processor
 * exceptions may be specifically caught.
 * @xsl.usage general
 */
public class XPathProcessorException extends XPathException
{
    static final long serialVersionUID = 1215509418326642603L;

  /**
   * Create an XPathProcessorException object that holds
   * an error message.
   * @param message The error message.
   */
  public XPathProcessorException(String message)
  {
    super(message);
  }
  

  /**
   * Create an XPathProcessorException object that holds
   * an error message, and another exception
   * that caused this exception.
   * @param message The error message.
   * @param e The exception that caused this exception.
   */
  public XPathProcessorException(String message, Exception e)
  {
    super(message, e);
  }
}
