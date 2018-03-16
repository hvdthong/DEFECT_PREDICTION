package org.apache.camel.component.bean;

import org.apache.camel.CamelException;

/**
 * Exception thrown if the bean could not be found in the registry.
 *
 * @version $Revision: 659771 $
 */
public class NoBeanAvailableException extends CamelException {
    private final String name;

    public NoBeanAvailableException(String name) {
        super("No bean available for endpoint: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
