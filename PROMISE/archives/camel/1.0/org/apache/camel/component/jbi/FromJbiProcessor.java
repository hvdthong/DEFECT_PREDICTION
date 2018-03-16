package org.apache.camel.component.jbi;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.servicemix.MessageExchangeListener;

import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;

/**
 * @version $Revision: 534145 $
 */
public class FromJbiProcessor implements MessageExchangeListener {
    private CamelContext context;
    private JbiBinding binding;
    private Processor processor;

    public FromJbiProcessor(CamelContext context, JbiBinding binding, Processor processor) {
        this.context = context;
        this.binding = binding;
        this.processor = processor;
    }

    public void onMessageExchange(MessageExchange messageExchange) throws MessagingException {
        try {
			JbiExchange exchange = new JbiExchange(context, binding, messageExchange);
			processor.process(exchange);
		} catch (Exception e) {
			throw new MessagingException(e);
		}
    }
}
