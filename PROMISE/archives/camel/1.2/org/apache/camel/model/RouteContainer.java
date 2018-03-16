package org.apache.camel.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * @version $Revision: 1.1 $
 */
public interface RouteContainer {
    /**
     * A list of routes
     *
     * @return
     */
    @XmlElementRef
    List<RouteType> getRoutes();

    /**
     * Sets the routes to use
     *
     * @param routes
     */
    void setRoutes(List<RouteType> routes);
}
