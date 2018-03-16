package org.apache.camel.component.validator;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.ResourceBasedComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.springframework.core.io.Resource;

/**
 * for validating XML against some schema
 *
 * @version $Revision: 655776 $
 */
public class ValidatorComponent extends ResourceBasedComponent {

    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        SpringValidator validator = new SpringValidator();
        Resource resource = resolveMandatoryResource(remaining);
        validator.setSchemaResource(resource);
        if (log.isDebugEnabled()) {
            log.debug(this + " using schema resource: " + resource);
        }
        configureValidator(validator, uri, remaining, parameters);
        return new ProcessorEndpoint(uri, this, validator);
    }

    protected void configureValidator(SpringValidator validator, String uri, String remaining, Map parameters) throws Exception {
        setProperties(validator, parameters);
    }
}
