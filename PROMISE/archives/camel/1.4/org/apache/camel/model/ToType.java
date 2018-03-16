package org.apache.camel.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.processor.SendProcessor;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;to/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "to")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToType extends ProcessorType<ProcessorType> {
    @XmlAttribute
    private String uri;
    @XmlAttribute
    private String ref;
    @XmlTransient
    private Endpoint endpoint;

    public ToType() {
    }

    public ToType(String uri) {
        setUri(uri);
    }

    public ToType(Endpoint endpoint) {
        setEndpoint(endpoint);
    }

    @Override
    public String toString() {
        return "To[" + getLabel() + "]";
    }

    @Override
    public String getShortName() {
        return "to";
    }

    @Override
    public String getLabel() {
        return FromType.description(getUri(), getRef(), getEndpoint());
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Endpoint endpoint = resolveEndpoint(routeContext);
        return new SendProcessor(endpoint);
    }

    public Endpoint resolveEndpoint(RouteContext context) {
        if (endpoint == null) {
            endpoint = context.resolveEndpoint(getUri(), getRef());
        }
        return endpoint;
    }

    public String getUri() {
        return uri;
    }

    /**
     * Sets the URI of the endpoint to use
     *
     * @param uri the endpoint URI to use
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRef() {
        return ref;
    }

    /**
     * Sets the name of the endpoint within the registry (such as the Spring
     * ApplicationContext or JNDI) to use
     *
     * @param ref the reference name to use
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public List<ProcessorType<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns the endpoint URI or the name of the reference to it
     */
    public Object getUriOrRef() {
        if (ObjectHelper.isNullOrBlank(uri)) {
            return uri;
        } else if (endpoint != null) {
            return endpoint.getEndpointUri();
        }
        return ref;
    }
}
