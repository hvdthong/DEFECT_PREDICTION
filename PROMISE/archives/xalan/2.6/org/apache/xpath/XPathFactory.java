package org.apache.xpath;

import javax.xml.transform.SourceLocator;

import org.apache.xml.utils.PrefixResolver;

/**
 * Factory class for creating an XPath.  Implementors of XPath derivatives
 * will need to make a derived class of this.
 * @xsl.usage advanced
 */
public interface XPathFactory
{

  /**
   * Create an XPath.
   *
   * @param exprString The XPath expression string.
   * @param locator The location of the expression string, mainly for diagnostic
   *                purposes.
   * @param prefixResolver This will be called in order to resolve prefixes 
   *        to namespace URIs.
   * @param type One of {@link org.apache.xpath.XPath#SELECT} or 
   *             {@link org.apache.xpath.XPath#MATCH}.
   *
   * @return an XPath ready for execution.
   */
  XPath create(String exprString, SourceLocator locator,
               PrefixResolver prefixResolver, int type);
}
