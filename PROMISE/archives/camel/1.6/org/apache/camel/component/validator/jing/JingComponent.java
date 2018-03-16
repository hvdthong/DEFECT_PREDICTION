package org.apache.camel.component.validator.jing;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.ResourceBasedComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.springframework.core.io.Resource;

/**
 * A component for validating XML payloads using the
 *
 * @version $Revision: 655776 $
 */
public class JingComponent extends ResourceBasedComponent {
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        JingValidator validator = new JingValidator();
        Resource resource = resolveMandatoryResource(remaining);
        validator.setSchemaResource(resource);
        if (log.isDebugEnabled()) {
            log.debug(this + " using schema resource: " + resource);
        }
        configureValidator(validator, uri, remaining, parameters);
        return new ProcessorEndpoint(uri, this, validator);
    }

    protected void configureValidator(JingValidator validator, String uri, String remaining, Map parameters) throws Exception {
        setProperties(validator, parameters);
    }
}
