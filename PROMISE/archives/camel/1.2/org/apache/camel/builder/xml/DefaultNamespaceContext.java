package org.apache.camel.builder.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * An implementation of {@link NamespaceContext} which uses a simple Map where
 * the keys are the prefixes and the values are the URIs
 *
 * @version $Revision: $
 */
public class DefaultNamespaceContext implements NamespaceContext {

    private final Map map;
    private final NamespaceContext parent;

    public DefaultNamespaceContext() {
        this(XPathFactory.newInstance());
    }

    public DefaultNamespaceContext(XPathFactory factory) {
        this.parent = factory.newXPath().getNamespaceContext();
        this.map = new HashMap();
    }

    public DefaultNamespaceContext(NamespaceContext parent, Map map) {
        this.parent = parent;
        this.map = map;
    }

    /**
     * A helper method to make it easy to create newly populated instances
     */
    public DefaultNamespaceContext add(String prefix, String uri) {
        map.put(prefix, uri);
        return this;
    }

    public String getNamespaceURI(String prefix) {
        String answer = (String) map.get(prefix);
        if (answer == null && parent != null) {
            return parent.getNamespaceURI(prefix);
        }
        return answer;
    }

    public String getPrefix(String namespaceURI) {
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (namespaceURI.equals(entry.getValue())) {
                return (String) entry.getKey();
            }
        }
        if (parent != null) {
            return parent.getPrefix(namespaceURI);
        }
        return null;
    }

    public Iterator getPrefixes(String namespaceURI) {
        Set set = new HashSet();
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (namespaceURI.equals(entry.getValue())) {
                set.add(entry.getKey());
            }
        }
        if (parent != null) {
            Iterator iter = parent.getPrefixes(namespaceURI);
            while (iter.hasNext()) {
                set.add(iter.next());
            }
        }
        return set.iterator();
    }

    public void setNamespacesFromDom(Element element) {
        Node parentNode = element.getParentNode();
        if (parentNode instanceof Element) {
            setNamespacesFromDom((Element) parentNode);
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
    }
}
