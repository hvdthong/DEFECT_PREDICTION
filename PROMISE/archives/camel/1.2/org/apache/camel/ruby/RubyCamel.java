package org.apache.camel.ruby;

import java.util.List;
import java.util.ArrayList;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * @version $Revision: 1.1 $
 */
public class RubyCamel {
    private static CamelContext camelContext;
    private static List<RouteBuilder> routes = new ArrayList<RouteBuilder>();

    public static List<RouteBuilder> getRoutes() {
        return routes;
    }

    public static void addRouteBuilder(RouteBuilder builder) {
        routes.add(builder);
    }

    public static CamelContext getCamelContext() {
        if (camelContext == null) {
            camelContext = new DefaultCamelContext();
        }
        return camelContext;
    }

    public static void setCamelContext(CamelContext camelContext) {
        RubyCamel.camelContext = camelContext;
    }
}
