package org.apache.camel.component.flatpack;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.ResourceBasedComponent;
import org.apache.camel.util.ObjectHelper;
import org.springframework.core.io.Resource;

/**
 * for working with fixed width and delimited files
 *
 * @version $Revision: 655776 $
 */
public class FlatpackComponent extends ResourceBasedComponent {

    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        boolean fixed = false;
        if (remaining.startsWith("fixed:")) {
            fixed = true;
            remaining = remaining.substring("fixed:".length());
        } else if (remaining.startsWith("delim:")) {
            remaining = remaining.substring("delim:".length());
        } else {
            remaining = "";
        }
        Resource resource = null;
        if (fixed) {
            resource = resolveMandatoryResource(remaining);
        } else {
            if (ObjectHelper.isNotNullAndNonEmpty(remaining)) {
                resource = getResourceLoader().getResource(remaining);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(this + " using flatpack map resource: " + resource);
        }
        FixedLengthEndpoint answer;
        if (fixed) {
            answer = new FixedLengthEndpoint(uri, resource);
        } else {
            answer = new DelimitedEndpoint(uri, resource);
        }
        answer.setCamelContext(getCamelContext());
        setProperties(answer, parameters);
        return answer;
    }
}
