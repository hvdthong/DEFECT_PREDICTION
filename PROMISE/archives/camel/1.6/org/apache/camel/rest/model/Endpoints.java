package org.apache.camel.rest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

/**
 * @version $Revision: 699772 $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Endpoints {
    @XmlElement(name = "endpoint")
    private List<EndpointLink> endpoints = new ArrayList<EndpointLink>();

    public Endpoints() {
    }

    public Endpoints(CamelContext camelContext) {
        this();
        load(camelContext);
    }

    @Override
    public String toString() {
        return "Endpoints" + endpoints;

    }

    public List<EndpointLink> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<EndpointLink> endpoints) {
        this.endpoints = endpoints;
    }

    public void load(CamelContext camelContext) {
        Collection<Endpoint> endpoints = camelContext.getSingletonEndpoints();
        for (Endpoint endpoint : endpoints) {
            addEndpoint(createEndpointLink(endpoint));
        }

    }

    protected EndpointLink createEndpointLink(Endpoint endpoint) {
        EndpointLink answer = new EndpointLink();
        answer.load(endpoint);
        return answer;
    }

    public void addEndpoint(EndpointLink endpointLink) {
        getEndpoints().add(endpointLink);
    }
}
