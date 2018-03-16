package org.apache.camel;

import java.util.List;

/**
 * Routes defined in the camel context.
 */
public interface Routes {

    /**
     * Gets the Camel context used.
     */
    CamelContext getContext();

    /**
     * Sets the Camel context to use.
     */
    void setContext(CamelContext context);

    /**
     * Gets the list of routes currently in the camel context.
     */
    List<Route> getRouteList() throws Exception;
}
