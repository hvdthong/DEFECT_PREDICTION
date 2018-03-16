package org.apache.camel.component.jbi;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.common.ServiceUnit;
import org.apache.servicemix.common.endpoints.ProviderEndpoint;

import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.namespace.QName;

/**
 * A JBI endpoint which when invoked will delegate to a Camel endpoint
 *
 * @version $Revision: 426415 $
 */
public class CamelJbiEndpoint extends ProviderEndpoint {
    private static final transient Log log = LogFactory.getLog(CamelJbiEndpoint.class);
    private Endpoint camelEndpoint;
    private JbiBinding binding;
    private Processor processor;

    public CamelJbiEndpoint(ServiceUnit serviceUnit, QName service, String endpoint, Endpoint camelEndpoint, JbiBinding binding, Processor processor) {
        super(serviceUnit, service, endpoint);
        this.processor = processor;
        this.camelEndpoint = camelEndpoint;
        this.binding = binding;
    }

    public CamelJbiEndpoint(ServiceUnit serviceUnit, Endpoint camelEndpoint, JbiBinding binding, Processor processor) {
        this(serviceUnit, SERVICE_NAME, camelEndpoint.getEndpointUri(), camelEndpoint, binding, processor);
    }

    protected void processInOnly(MessageExchange exchange, NormalizedMessage in) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Received exchange: " + exchange);
        }
        JbiExchange camelExchange = new JbiExchange(camelEndpoint.getContext(), binding, exchange);
        processor.process(camelExchange);
    }

    protected void processInOut(MessageExchange exchange, NormalizedMessage in, NormalizedMessage out) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Received exchange: " + exchange);
        }
        /*
         * ToDo
         */
    }
}
