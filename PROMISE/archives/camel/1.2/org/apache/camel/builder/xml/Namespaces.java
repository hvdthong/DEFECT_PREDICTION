package org.apache.camel.builder.xml;

import org.apache.camel.util.ObjectHelper;

/**
 * @version $Revision: $
 */
public class Namespaces {

    
    /**
     * Utility classes should not have a public constructor.
     */
    private Namespaces() {        
    }

    /**
     * Returns true if the given namespaceURI is empty or if it matches the
     * given expected namespace
     */
    public static boolean isMatchingNamespaceOrEmptyNamespace(String namespaceURI, String expectedNamespace) {
        return ObjectHelper.isNullOrBlank(namespaceURI) || namespaceURI.equals(expectedNamespace);
    }

}
