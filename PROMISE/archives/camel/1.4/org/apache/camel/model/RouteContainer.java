package org.apache.camel.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * Container to hold {@link org.apache.camel.model.RouteType Route}.
 *
 * @version $Revision: 660266 $
 */
public interface RouteContainer {
    /**
     * Returns the routes
     */
    @XmlElementRef
    List<RouteType> getRoutes();

    /**
     * Sets the routes to use
     */
    void setRoutes(List<RouteType> routes);
}
