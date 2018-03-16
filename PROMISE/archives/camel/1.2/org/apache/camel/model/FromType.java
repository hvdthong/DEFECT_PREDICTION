package org.apache.camel.model;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.util.ObjectHelper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents an XML &lt;to/&gt; element
 * 
 * @version $Revision: $
 */
@XmlRootElement(name = "from")
@XmlAccessorType(XmlAccessType.FIELD)
public class FromType {
    @XmlAttribute
    private String uri;
    @XmlAttribute
    private String ref;
    @XmlTransient
    private Endpoint endpoint;

    public FromType() {
    }

    public FromType(String uri) {
        setUri(uri);
    }

    public FromType(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return "From[" + getLabel() + "]";
    }

    public String getLabel() {
        return description(getUri(), getRef(), getEndpoint());
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

    /**
     * Returns the endpoint URI or the name of the reference to it
     */
    public Object getUriOrRef() {
        if (ObjectHelper.isNullOrBlank(uri)) {
            return uri;
        }
        else if (endpoint != null) {
            return endpoint.getEndpointUri();
        }
        return ref;
    }

    protected static String description(String uri, String ref, Endpoint endpoint) {
        if (ref != null) {
            return "ref:" + ref;
        } else if (endpoint != null) {
            return endpoint.getEndpointUri();
        } else if (uri != null) {
            return uri;
        } else {
            return "no uri or ref supplied!";
        }
    }
}
