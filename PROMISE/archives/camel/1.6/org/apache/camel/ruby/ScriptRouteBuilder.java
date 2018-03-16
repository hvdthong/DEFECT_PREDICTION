package org.apache.camel.ruby;

import org.apache.camel.Endpoint;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.RouteType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provide some helper methods for building routes from scripting languages
 * with a minimum amount of noise using state for the current node in the DSL
 *
 * @version $Revision: 630591 $
 */
public abstract class ScriptRouteBuilder extends RouteBuilder {
    private static final transient Log LOG = LogFactory.getLog(ScriptRouteBuilder.class);
    protected ProcessorType node;

    public ScriptRouteBuilder() {
    }

    public ScriptRouteBuilder(CamelContext context) {
        super(context);
    }

    @Override
    protected void configureRoute(RouteType route) {
        super.configureRoute(route);
        this.node = route;
    }

    public ProcessorType to(String uri) {
        return getNode().to(uri);
    }
    
    public ProcessorType to(Endpoint endpoint) {
        return getNode().to(endpoint);
    }

    public ProcessorType getNode() {
        if (node == null) {
            throw new IllegalStateException("You must define a route first via the from() method");
        }
        return node;
    }

    public void setNode(ProcessorType node) {
        this.node = node;

        LOG.info("Node is now: " + node);
    }
}
