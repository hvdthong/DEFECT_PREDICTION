package org.apache.camel.impl;

import org.apache.camel.builder.RouteBuilder;

/**
 * A helper class, usually used for testing which does not create any routes.
 *
 * @version $Revision: 1.1 $
 */
public class NoRouteBuilder extends RouteBuilder {
    private static final NoRouteBuilder INSTANCE = new NoRouteBuilder();

    public static NoRouteBuilder getInstance() {
        return INSTANCE;
    }

    public void configure() throws Exception {
    }
}
