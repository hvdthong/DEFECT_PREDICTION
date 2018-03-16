package org.apache.camel.impl;

import org.apache.camel.Endpoint;

/**
 * A proxy creation failed trying to create a proxy of a given type and on an given endpoint
 *
 * @version $Revision: 724293 $
 */
public class ProxyInstantiationException extends RuntimeException {
    private final Class<?> type;
    private final Endpoint endpoint;

    public ProxyInstantiationException(Class<?> type, Endpoint endpoint, Throwable cause) {
        super("Could not instantiate proxy of type " + type.getName() + " on endpoint " + endpoint, cause);
        this.type = type;
        this.endpoint = endpoint;
    }

    public Class<?> getType() {
        return type;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }
}
