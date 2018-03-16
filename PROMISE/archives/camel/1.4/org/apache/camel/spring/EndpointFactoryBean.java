package org.apache.camel.spring;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.model.IdentifiedType;
import org.springframework.beans.factory.FactoryBean;

import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * A {@link FactoryBean} which instantiates {@link Endpoint} objects
 *
 * @version $Revision: 641676 $
 */
@XmlRootElement(name = "endpoint")
@XmlAccessorType(XmlAccessType.FIELD)
public class EndpointFactoryBean extends IdentifiedType implements FactoryBean, CamelContextAware {
    @XmlAttribute
    private String uri;
    @XmlTransient
    private CamelContext context;
    @XmlTransient
    private Endpoint endpoint;
    @XmlTransient
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

    public CamelContext getCamelContext() {
        return context;
    }

    /**
     * Sets the context to use to resolve endpoints
     *
     * @param context the context used to resolve endpoints
     */
    public void setCamelContext(CamelContext context) {
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
        Endpoint endpoint = context.getEndpoint(uri);
        if (endpoint == null) {
            throw new NoSuchEndpointException(uri);
        }
        return endpoint;
    }
}
