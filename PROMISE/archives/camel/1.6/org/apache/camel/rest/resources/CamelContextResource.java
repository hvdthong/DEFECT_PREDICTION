package org.apache.camel.rest.resources;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sun.jersey.spi.inject.Inject;
import com.sun.jersey.spi.resource.Singleton;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.model.RouteType;
import org.apache.camel.model.RoutesType;
import org.apache.camel.rest.model.Endpoints;

/**
 * The resource for the CamelContext
 *
 * @version $Revision: 700513 $
 */
@Path("/")
@Singleton
@Produces({"text/html", "application/xml", "application/json"})
public class CamelContextResource {

    private final CamelContext camelContext;

    public CamelContextResource(@Inject CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public String getName() {
        return camelContext.getName();
    }

    /**
     * Returns a list of endpoints available in this context
     *
     * @return
     */
    @GET
    @Path("endpoints")
    public Endpoints getEndpoint() {
        return new Endpoints(camelContext);
    }

    /**
     * Looks up an individual endpoint
     */
    @GET
    @Path("endpoint/{id}")
    public EndpointResource getEndpoint(@PathParam("id") String id) {
        Endpoint endpoint = getCamelContext().getEndpoint(id);
        if (endpoint != null) {
            return new EndpointResource(endpoint);
        } else {
            return null;
        }
    }

    /**
     * Returns the routes currently active within this context
     *
     * @return
     */
    @GET
    @Path("routes")
    public RoutesType getRouteDefinitions() {
        RoutesType answer = new RoutesType();
        if (camelContext != null) {
            List<RouteType> list = camelContext.getRouteDefinitions();
            answer.setRoutes(list);
        }
        return answer;
    }

}
