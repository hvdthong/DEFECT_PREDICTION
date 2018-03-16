package org.apache.camel.component;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * A useful base class for components which depend on a resource
 * such as things like Velocity or XQuery based components.
 *
 * @version $Revision: 673477 $
 */
public abstract class ResourceBasedComponent extends DefaultComponent<Exchange> {
    protected final transient Log log = LogFactory.getLog(getClass());
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
