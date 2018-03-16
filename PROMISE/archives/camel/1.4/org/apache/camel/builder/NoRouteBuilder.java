package org.apache.camel.builder;


/**
 * A helper class, usually used for testing which does not create any routes.
 *
 * @version $Revision: 663018 $
 */
public class NoRouteBuilder extends RouteBuilder {
    private static final NoRouteBuilder INSTANCE = new NoRouteBuilder();

    public static NoRouteBuilder getInstance() {
        return INSTANCE;
    }

    public void configure() throws Exception {
    }
}
