package org.apache.camel.component.jbi;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.servicemix.jbi.resolver.URIResolver;

import javax.jbi.component.ComponentContext;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessageExchangeFactory;
import javax.jbi.messaging.MessagingException;

/**
 * A @{link Processor} which takes a Camel {@link Exchange} and invokes it into JBI using the straight JBI API
 *
 * @version $Revision: 534145 $
 */
public class ToJbiProcessor implements Processor {
    private JbiBinding binding;
    private ComponentContext componentContext;
    private String destinationUri;

    public ToJbiProcessor(JbiBinding binding, ComponentContext componentContext, String destinationUri) {
        this.binding = binding;
        this.componentContext = componentContext;
        this.destinationUri = destinationUri;
    }

    public void process(Exchange exchange) {
        try {
            DeliveryChannel deliveryChannel = componentContext.getDeliveryChannel();
            MessageExchangeFactory exchangeFactory = deliveryChannel.createExchangeFactory();
            MessageExchange messageExchange = binding.makeJbiMessageExchange(exchange, exchangeFactory);

            URIResolver.configureExchange(messageExchange, componentContext, destinationUri);
            deliveryChannel.sendSync(messageExchange);
        }
        catch (MessagingException e) {
            throw new JbiException(e);
        }
    }
}
