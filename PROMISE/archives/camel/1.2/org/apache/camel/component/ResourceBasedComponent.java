package org.apache.camel.component;

import org.apache.camel.Exchange;
import org.apache.camel.component.validator.ValidatorComponent;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @version $Revision: 1.1 $
 */
public abstract class ResourceBasedComponent extends DefaultComponent<Exchange> {
    protected static final transient Log LOG = LogFactory.getLog(ValidatorComponent.class);
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected Resource resolveMandatoryResource(String uri) {
        Resource resource = getResourceLoader().getResource(uri);
        if (resource == null) {
            throw new IllegalArgumentException("Could not find resource for URI: " + uri + " using: " + getResourceLoader());
        } else {
            return resource;
        }

    }
}
