package org.apache.camel.spi;

import java.util.Map;

/**
 * Represents an object which is aware of the namespaces in which its used such as
 * XPath and XQuery type expressions so that the current namespace context can be injected
 *
 * @version $Revision: 640438 $
 */
public interface NamespaceAware {

    /**
     * Injects the XML Namespaces of prefix -> uri mappings
     *
     * @param namespaces the XML namespaces with the key of prefixes and the value the URIs
     */
    void setNamespaces(Map<String, String> namespaces);
}
