package org.apache.camel.util;

import java.io.IOException;

/**
 * Thrown if no factory resource is available for the given URI
 *
 * @version $Revision: 563607 $
 */
public class NoFactoryAvailableException extends IOException {
    private final String uri;

    public NoFactoryAvailableException(String uri) {
        super("Could not find factory class for resource: " + uri);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
