package org.apache.camel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * defines the processing used on an inbound message exchange
 * from a specific {@link Endpoint} within a {@link CamelContext}
 *
 * @version $Revision: 642753 $
 */
public class Route<E extends Exchange> {
    public static final String PARENT_PROPERTY = "parent";
    public static final String GROUP_PROPERTY = "group";

    private final Map<String, Object> properties = new HashMap<String, Object>(16);
    private Endpoint<E> endpoint;
    private List<Service> services = new ArrayList<Service>();

    public Route(Endpoint<E> endpoint) {
        this.endpoint = endpoint;
    }

    public Route(Endpoint<E> endpoint, Service... services) {
        this(endpoint);
        for (Service service : services) {
            addService(service);
        }
    }

    @Override
    public String toString() {
        return "Route";
    }

    public Endpoint<E> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint<E> endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * This property map is used to associate information about
     * the route.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    public List<Service> getServicesForRoute() throws Exception {
        List<Service> servicesForRoute = new ArrayList<Service>(getServices());
        addServices(servicesForRoute);
        return servicesForRoute;
    }

    /**
     * Returns the additional services required for this particular route
     */
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void addService(Service service) {
        getServices().add(service);
    }

    /**
     * Strategy method to allow derived classes to lazily load services for the route
     */
    protected void addServices(List<Service> services) throws Exception {
    }
}
