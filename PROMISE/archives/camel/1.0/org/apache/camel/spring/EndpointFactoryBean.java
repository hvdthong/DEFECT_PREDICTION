package org.apache.camel.spring;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import static org.apache.camel.util.ObjectHelper.notNull;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} which instantiates {@link Endpoint} objects
 *
 * @version $Revision: 1.1 $
 */
public class EndpointFactoryBean implements FactoryBean {
    private CamelContext context;
    private String uri;
    private Endpoint endpoint;
    private boolean singleton;

    public Object getObject() throws Exception {
        if (endpoint == null) {
            endpoint = createEndpoint();
        }
        return endpoint;
    }

    public Class getObjectType() {
        return Endpoint.class;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public CamelContext getContext() {
        return context;
    }

    /**
     * Sets the context to use to resolve endpoints
     *
     * @param context the context used to resolve endpoints
     */
    public void setContext(CamelContext context) {
        this.context = context;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public String getUri() {
        return uri;
    }

    /**
     * Sets the URI to use to resolve the endpoint
     *
     * @param uri the URI used to set the endpoint
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    protected Endpoint createEndpoint() {
        notNull(context, "context");
        notNull(uri, "uri");
        return context.getEndpoint(uri);
    }

}
