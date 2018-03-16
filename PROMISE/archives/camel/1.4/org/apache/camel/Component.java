package org.apache.camel;

/**
 * a factory of {@link Endpoint} objects.
 * 
 * @version $Revision: 630591 $
 */
public interface Component<E extends Exchange> {

    /**
     * Returns the context
     * 
     * @return the context of this component
     */
    CamelContext getCamelContext();

    /**
     * The {@link CamelContext} is injected into the component when it is added
     * to it
     */
    void setCamelContext(CamelContext context);

    /**
     * Attempt to resolve an endpoint for the given URI if the component is
     * capable of handling the URI
     * 
     * @param uri the URI to create
     * @return a newly created endpoint or null if this component cannot create
     *         instances of the given uri
     */
    Endpoint<E> createEndpoint(String uri) throws Exception;
}
