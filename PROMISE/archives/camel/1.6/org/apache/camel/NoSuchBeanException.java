package org.apache.camel;

import org.apache.camel.spi.Registry;

/**
 * A runtime exception if a given bean could not be found in the {@link Registry}
 *
 * @version $Revision: 630591 $
 */
public class NoSuchBeanException extends RuntimeCamelException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final String name;

    public NoSuchBeanException(String name) {
        super("No bean could be found in the registry for: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
