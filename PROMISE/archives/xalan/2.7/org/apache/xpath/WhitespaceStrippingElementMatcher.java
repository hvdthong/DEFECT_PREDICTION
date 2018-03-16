package org.apache.xpath;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;

/**
 * A class that implements this interface can tell if a given element should 
 * strip whitespace nodes from it's children.
 */
public interface WhitespaceStrippingElementMatcher
{
  /**
   * Get information about whether or not an element should strip whitespace.
   *
   * @param support The XPath runtime state.
   * @param targetElement Element to check
   *
   * @return true if the whitespace should be stripped.
   *
   * @throws TransformerException
   */
  public boolean shouldStripWhiteSpace(
          XPathContext support, Element targetElement) throws TransformerException;
  
  /**
   * Get information about whether or not whitespace can be stripped.
   *
   * @return true if the whitespace can be stripped.
   */
  public boolean canStripWhiteSpace();
}
