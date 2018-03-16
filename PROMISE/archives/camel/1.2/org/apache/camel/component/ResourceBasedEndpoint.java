package org.apache.camel.component;

import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.validator.ValidatorComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * A useful base class for endpoints which depend on a resource
 * such as things like Velocity or XQuery based components
 *
 * @version $Revision: 1.1 $
 */
public abstract class ResourceBasedEndpoint extends ProcessorEndpoint {
    protected static final transient Log LOG = LogFactory.getLog(ValidatorComponent.class);
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final String resourceUri;
    private Resource resource;

    public ResourceBasedEndpoint(String endpointUri, Component component, String resourceUri, Processor processor) {
        super(endpointUri, component, processor);
        this.resourceUri = resourceUri;
    }
    public Resource getResource() {
        if (resource == null) {
            resource = getResourceLoader().getResource(resourceUri);
            if (resource == null) {
                throw new IllegalArgumentException("Could not find resource for URI: " + resourceUri + " using: " + getResourceLoader());
            }
        }
        return resource;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
