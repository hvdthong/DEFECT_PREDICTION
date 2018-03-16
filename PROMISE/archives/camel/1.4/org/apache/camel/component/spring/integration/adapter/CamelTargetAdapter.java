package org.apache.camel.component.spring.integration.adapter;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.spring.integration.SpringIntegrationBinding;
import org.apache.camel.component.spring.integration.SpringIntegrationExchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.message.Message;

/**
 * CamelTargeAdapter will redirect the Spring Integration message to the Camel context.
 * When we inject the camel context into it, we need also specify the Camel endpoint url
 * we will route the Spring Integration message to the Camel context
 * @author Willem Jiang
 *
 * @version $Revision: 664245 $
 */
public class CamelTargetAdapter extends AbstractCamelAdapter {

    private final Log logger = LogFactory.getLog(this.getClass());
    private ProducerTemplate<Exchange> camelTemplate;
    private Endpoint camelEndpoint;

    public ProducerTemplate<Exchange> getCamelTemplate() {
        if (camelTemplate == null) {
            CamelContext ctx = getCamelContext();
            if (ctx == null) {
                ctx = new DefaultCamelContext();
            }
            camelTemplate = ctx.createProducerTemplate();
        }
        return camelTemplate;
    }

    public Message<?> handle(Message<?> request) {
        ExchangePattern pattern;
        if (isExpectReply()) {
            pattern = ExchangePattern.InOut;
        } else {
            pattern = ExchangePattern.InOnly;
        }
        Exchange inExchange = new SpringIntegrationExchange(getCamelContext(), pattern);
        SpringIntegrationBinding.storeToCamelMessage(request, inExchange.getIn());
        Exchange outExchange = getCamelTemplate().send(getCamelEndpointUri(), inExchange);
        Message response = null;
        if (isExpectReply()) {
            response = SpringIntegrationBinding.storeToSpringIntegrationMessage(outExchange.getOut());
        }
        return response;
    }

}
