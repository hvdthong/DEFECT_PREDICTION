package org.apache.camel.component.spring.integration;

import java.util.Map;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link SpringIntegrationEndpoint}. It holds the
 * list of named direct endpoints.
 *
 * @version $Revision: 652240 $
 */
public class SpringIntegrationComponent extends DefaultComponent<SpringIntegrationExchange> {

    protected SpringIntegrationEndpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        SpringIntegrationEndpoint endpoint = new SpringIntegrationEndpoint(uri, remaining, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
