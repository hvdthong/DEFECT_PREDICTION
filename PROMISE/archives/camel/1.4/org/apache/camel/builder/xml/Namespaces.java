package org.apache.camel.builder.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.apache.camel.model.language.XPathExpression;
import org.apache.camel.model.language.XQueryExpression;
import org.apache.camel.spi.NamespaceAware;
import org.apache.camel.util.ObjectHelper;

/**
 * A helper class for working with namespaces or creating namespace based expressions
 *
 * @version $Revision: 660275 $
 */
public class Namespaces {

    private Map<String, String> namespaces = new HashMap<String, String>();

    /**
     * Creates a namespaces object from the given XML element
     *
     * @param element the XML element representing the XPath namespace context
     */
    public Namespaces(Element element) {
        add(element);
    }

    /**
     * Creates a namespace context with a single prefix and URI
     */
    public Namespaces(String prefix, String uri) {
        add(prefix, uri);
    }

    /**
     * Returns true if the given namespaceURI is empty or if it matches the
     * given expected namespace
     */
    public static boolean isMatchingNamespaceOrEmptyNamespace(String namespaceURI, String expectedNamespace) {
        return ObjectHelper.isNullOrBlank(namespaceURI) || namespaceURI.equals(expectedNamespace);
    }

    public Namespaces add(String prefix, String uri) {
        namespaces.put(prefix, uri);
        return this;
    }

    public Namespaces add(Element element) {
        Node parentNode = element.getParentNode();
        if (parentNode instanceof org.w3c.dom.Element) {
            add((Element) parentNode);
        }
        NamedNodeMap attributes = element.getAttributes();
        int size = attributes.getLength();
        for (int i = 0; i < size; i++) {
            Attr node = (Attr) attributes.item(i);
            String name = node.getName();
            if (name.startsWith("xmlns:")) {
                String prefix = name.substring("xmlns:".length());
                String uri = node.getValue();
                add(prefix, uri);
            }
        }
        return this;
    }

    /**
     * Creates the XPath expression using the current namespace context
     */
    public XPathExpression xpath(String expression) {
        XPathExpression answer = new XPathExpression(expression);
        configure(answer);
        return answer;
    }

    /**
     * Creates the XPath expression using the current namespace context
     */
    public XPathExpression xpath(String expression, Class<?> resultType) {
        XPathExpression answer = xpath(expression);
        answer.setResultType(resultType);
        return answer;
    }

    /**
     * Creates the XQuery expression using the current namespace context
     */
    public XQueryExpression xquery(String expression) {
        XQueryExpression answer = new XQueryExpression(expression);
        configure(answer);
        return answer;
    }

    /**
     * Creates the XQuery expression using the current namespace context
     * and the given expected return type
     */
    public XQueryExpression xquery(String expression, Class<?> resultType) {
        XQueryExpression answer = new XQueryExpression(expression);
        answer.setResultType(resultType);
        configure(answer);
        return answer;
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    /**
     * Configures the namespace aware object
     */
    public void configure(NamespaceAware namespaceAware) {
        namespaceAware.setNamespaces(getNamespaces());

    }
}
