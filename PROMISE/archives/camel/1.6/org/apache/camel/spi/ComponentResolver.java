package org.apache.camel.spi;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;

/**
 * Represents a resolver of components from a URI to be able to auto-load them using some
 * discovery mechanism like {@link org.apache.camel.impl.DefaultComponentResolver}
 *
 * @version $Revision: 659849 $
 */
public interface ComponentResolver<E extends Exchange> {
    
    /**
     * Attempts to resolve the component for the given URI
     *
     * @param name the component name to resolve
     * @param context the context to load the component if it can be resolved
     * @return the component which is added to the context or null if it can not be resolved
     * @throws Exception is thrown if the the component could not be loaded
     */
    Component<E> resolveComponent(String name, CamelContext context) throws Exception;
}
