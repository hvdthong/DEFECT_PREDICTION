package org.apache.camel.component.jcr;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultExchange;

/**
 * A component for integrating with JSR-170 (JCR) compliant content repositories
 */
public class JcrComponent extends DefaultComponent<DefaultExchange> {

    /**
     * Property key for specifying the name of a node in the repository 
     */
    public static final String NODE_NAME = "org.apache.camel.component.jcr.node_name";

    @Override @SuppressWarnings("unchecked")
    protected Endpoint<DefaultExchange> createEndpoint(String uri, String remaining, Map properties) throws Exception {
        return new JcrEndpoint(uri, this);
    }

}
