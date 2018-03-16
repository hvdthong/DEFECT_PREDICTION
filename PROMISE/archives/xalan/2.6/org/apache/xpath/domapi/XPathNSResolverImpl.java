package org.apache.xpath.domapi;

import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathNSResolver;

/**
 *
 * The class provides an implementation XPathNSResolver according 
 * to the DOM L3 XPath API Specification, Working Draft 28, March 2002.
 * 
 * 
 * <p>The <code>XPathNSResolver</code> interface permit <code>prefix</code> 
 * strings in the expression to be properly bound to 
 * <code>namespaceURI</code> strings. <code>XPathEvaluator</code> can 
 * construct an implementation of <code>XPathNSResolver</code> from a node, 
 * or the interface may be implemented by any application.</p>
 * 
 * @see org.w3c.dom.xpath.XPathNSResolver
 * @xsl.usage experimental
 */
public class XPathNSResolverImpl extends PrefixResolverDefault implements XPathNSResolver {

	/**
	 * Constructor for XPathNSResolverImpl.
	 * @param xpathExpressionContext
	 */
	public XPathNSResolverImpl(Node xpathExpressionContext) {
		super(xpathExpressionContext);
	}

	/**
	 * @see org.w3c.dom.xpath.XPathNSResolver#lookupNamespaceURI(String)
	 */
	public String lookupNamespaceURI(String prefix) {
		return super.getNamespaceForPrefix(prefix);
	}

}
