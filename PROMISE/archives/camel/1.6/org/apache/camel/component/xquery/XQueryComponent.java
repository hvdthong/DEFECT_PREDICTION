package org.apache.camel.component.xquery;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.ResourceBasedComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.springframework.core.io.Resource;


/**
 * for performing transforming messages
 *
 * @version $Revision: 655776 $
 */
public class XQueryComponent extends ResourceBasedComponent {

    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        Resource resource = resolveMandatoryResource(remaining);
        if (log.isDebugEnabled()) {
            log.debug(this + " using schema resource: " + resource);
        }
        XQueryBuilder xslt = XQueryBuilder.xquery(resource.getURL());
        configureXslt(xslt, uri, remaining, parameters);
        return new ProcessorEndpoint(uri, this, xslt);
    }

    protected void configureXslt(XQueryBuilder xQueryBuilder, String uri, String remaining, Map parameters) throws Exception {
        setProperties(xQueryBuilder, parameters);
    }
}
