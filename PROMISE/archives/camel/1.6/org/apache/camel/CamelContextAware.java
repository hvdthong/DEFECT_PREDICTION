package org.apache.camel;

/**
 * An interface to represent an object which wishes to be injected with
 * a {@link CamelContext} such as when working with Spring or Guice
 *
 * @version $Revision: 630591 $
 */
public interface CamelContextAware {

    /**
     * Injects the {@link CamelContext}
     *
     * @param camelContext
     */
    void setCamelContext(CamelContext camelContext);
}
