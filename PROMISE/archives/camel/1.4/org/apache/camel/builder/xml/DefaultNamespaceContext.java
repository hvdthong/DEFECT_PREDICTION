package org.apache.camel.builder.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.spi.NamespaceAware;

/**
 * An implementation of {@link NamespaceContext} which uses a simple Map where
 * the keys are the prefixes and the values are the URIs
 *
 * @version $Revision: 640438 $
 */
public class DefaultNamespaceContext implements NamespaceContext, NamespaceAware {

    private final Map<String, String> map;
    private final NamespaceContext parent;

    public DefaultNamespaceContext() {
        this(XPathFactory.newInstance());
    }

    public DefaultNamespaceContext(XPathFactory factory) {
        this.parent = factory.newXPath().getNamespaceContext();
        this.map = new HashMap<String, String>();
    }

    public DefaultNamespaceContext(NamespaceContext parent, Map<String, String> map) {
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
        String answer = map.get(prefix);
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

    public void setNamespaces(Map<String, String> namespaces) {
        map.putAll(namespaces);
    }
}
