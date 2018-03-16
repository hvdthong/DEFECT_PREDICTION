package org.apache.camel.component.bean;

import org.apache.camel.CamelException;

/**
 * @version $Revision: 1.1 $
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
