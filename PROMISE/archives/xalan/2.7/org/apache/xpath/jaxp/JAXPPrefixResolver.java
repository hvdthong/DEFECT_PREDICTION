package org.apache.xpath.jaxp;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.apache.xml.utils.PrefixResolver;

import javax.xml.namespace.NamespaceContext;

/**
 * <meta name="usage" content="general"/>
 * This class implements a Default PrefixResolver which
 * can be used to perform prefix-to-namespace lookup
 * for the XPath object.
 * This class delegates the resolution to the passed NamespaceContext
 */
public class JAXPPrefixResolver implements PrefixResolver
{

    private NamespaceContext namespaceContext;
    

    public JAXPPrefixResolver ( NamespaceContext nsContext ) {
        this.namespaceContext = nsContext;
    } 


    public String getNamespaceForPrefix( String prefix ) {
        return namespaceContext.getNamespaceURI( prefix );
    }

    /**
     * Return the base identifier.
     *
     * @return null
     */
    public String getBaseIdentifier() {
        return null;
    }

    /**
     * @see PrefixResolver#handlesNullPrefixes() 
     */
    public boolean handlesNullPrefixes() {
        return false;
    }


    /**
     * The URI for the XML namespace.
     * (Duplicate of that found in org.apache.xpath.XPathContext). 
     */
     
    public static final String S_XMLNAMESPACEURI =


    /**
     * Given a prefix and a Context Node, get the corresponding namespace.
     * Warning: This will not work correctly if namespaceContext
     * is an attribute node.
     * @param prefix Prefix to resolve.
     * @param namespaceContext Node from which to start searching for a
     * xmlns attribute that binds a prefix to a namespace.
     * @return Namespace that prefix resolves to, or null if prefix
     * is not bound.
     */
    public String getNamespaceForPrefix(String prefix,
                                      org.w3c.dom.Node namespaceContext) {
        Node parent = namespaceContext;
        String namespace = null;

        if (prefix.equals("xml")) {
            namespace = S_XMLNAMESPACEURI;
        } else {
            int type;

            while ((null != parent) && (null == namespace)
                && (((type = parent.getNodeType()) == Node.ELEMENT_NODE)
                    || (type == Node.ENTITY_REFERENCE_NODE))) {

                if (type == Node.ELEMENT_NODE) {
                    NamedNodeMap nnm = parent.getAttributes();

                    for (int i = 0; i < nnm.getLength(); i++) {
                        Node attr = nnm.item(i);
                        String aname = attr.getNodeName();
                        boolean isPrefix = aname.startsWith("xmlns:");

                        if (isPrefix || aname.equals("xmlns")) {
                            int index = aname.indexOf(':');
                            String p =isPrefix ?aname.substring(index + 1) :"";

                            if (p.equals(prefix)) {
                                namespace = attr.getNodeValue();
                                break;
                            }
                        }
                    }
                }

                parent = parent.getParentNode();
            }
        }
        return namespace;
    }

}

