package org.w3c.dom.xpath;


/**
 * The <code>XPathNSResolver</code> interface permit <code>prefix</code> 
 * strings in the expression to be properly bound to 
 * <code>namespaceURI</code> strings. <code>XPathEvaluator</code> can 
 * construct an implementation of <code>XPathNSResolver</code> from a node, 
 * or the interface may be implemented by any application.
 */
public interface XPathNSResolver {
    /**
     * Look up the namespace URI associated to the given namespace prefix. The 
     * XPath evaluator must never call this with a <code>null</code> or 
     * empty argument, because the result of doing this is undefined.
     * @param prefix The prefix to look for.
     * @return Returns the associated namespace URI or <code>null</code> if 
     *   none is found.
     */
    public String lookupNamespaceURI(String prefix);

}
