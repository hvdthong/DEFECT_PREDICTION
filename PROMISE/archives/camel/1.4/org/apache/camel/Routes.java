package org.apache.camel;

import java.util.List;

public interface Routes {

    CamelContext getContext();

    void setContext(CamelContext context);

    List<Route> getRouteList() throws Exception;
}
