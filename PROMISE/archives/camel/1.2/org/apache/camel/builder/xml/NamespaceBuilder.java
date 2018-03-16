package org.apache.camel.builder.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A helper class for creating namespaces which can then be used to create XPath expressions
 *
 * @version $Revision: 1.1 $
 */
public class NamespaceBuilder {
    private Map<String, String> namespaces = new HashMap<String, String>();

    public static NamespaceBuilder namespaceContext() {
        return new NamespaceBuilder();
    }

    public static NamespaceBuilder namespaceContext(String prefix, String uri) {
        return new NamespaceBuilder().namespace(prefix, uri);
    }

    public NamespaceBuilder namespace(String prefix, String uri) {
        namespaces.put(prefix, uri);
        return this;
    }

    /**
     * Creates a new XPath expression using the current namespaces
     *
     * @param xpath the XPath expression
     * @return a new XPath expression
     */
    public XPathBuilder xpath(String xpath) {
        XPathBuilder answer = XPathBuilder.xpath(xpath);
        Set<Map.Entry<String, String>> entries = namespaces.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            answer.namespace(entry.getKey(), entry.getValue());
        }
        return answer;
    }
}
