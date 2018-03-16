package org.apache.camel.component.jbi;

import org.apache.camel.Exchange;

import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessageExchangeFactory;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

/**
 * The binding of how Camel messages get mapped to JBI and back again
 *
 * @version $Revision: 522517 $
 */
public class JbiBinding {
    /**
     * Extracts the body from the given normalized message
     */
    public Object extractBodyFromJbi(JbiExchange exchange, NormalizedMessage normalizedMessage) {
        return normalizedMessage.getContent();
    }

    public MessageExchange makeJbiMessageExchange(Exchange camelExchange, MessageExchangeFactory exchangeFactory) throws MessagingException {
        MessageExchange jbiExchange = createJbiMessageExchange(camelExchange, exchangeFactory);
        NormalizedMessage normalizedMessage = jbiExchange.getMessage("in");
        if (normalizedMessage == null) {
            normalizedMessage = jbiExchange.createMessage();
            jbiExchange.setMessage(normalizedMessage, "in");
        }
        normalizedMessage.setContent(getJbiInContent(camelExchange));
        addJbiHeaders(jbiExchange, normalizedMessage, camelExchange);
        return jbiExchange;
    }

    protected MessageExchange createJbiMessageExchange(Exchange camelExchange, MessageExchangeFactory exchangeFactory) throws MessagingException {
        return exchangeFactory.createInOnlyExchange();
    }

    protected Source getJbiInContent(Exchange camelExchange) {
        Object value = camelExchange.getIn().getBody();
        if (value instanceof String) {
            return new StreamSource(new StringReader(value.toString()));
        }
        return camelExchange.getIn().getBody(Source.class);
    }

    protected void addJbiHeaders(MessageExchange jbiExchange, NormalizedMessage normalizedMessage, Exchange camelExchange) {
        Set<Map.Entry<String, Object>> entries = camelExchange.getIn().getHeaders().entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            normalizedMessage.setProperty(entry.getKey(), entry.getValue());
        }
    }
}

