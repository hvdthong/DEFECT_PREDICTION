package org.apache.camel.component.mina;

import org.apache.camel.Exchange;

/**
 * Helper to get and set the correct payload when transfering data using camel-mina.
 * Always use this helper instead of direct access on the exchange object.
 * <p/>
 * This helper ensures that we can also transfer exchange objects over the wire using the
 * <tt>exchangePayload=true</tt> option.
 *
 * @see org.apache.camel.component.mina.MinaPayloadHolder
 * @version $Revision: 641198 $
 */
public final class MinaPayloadHelper {
    private MinaPayloadHelper() {
    }

    public static Object getIn(MinaEndpoint endpoint, Exchange exchange) {
        if (endpoint.isTransferExchange()) {
            return MinaPayloadHolder.marshal(exchange);
        } else {
            return exchange.getIn().getBody();
        }
    }

    public static Object getOut(MinaEndpoint endpoint, Exchange exchange) {
        if (endpoint.isTransferExchange()) {
            return MinaPayloadHolder.marshal(exchange);
        } else {
            return exchange.getOut().getBody();
        }
    }

    public static void setIn(Exchange exchange, Object payload) {
        if (payload instanceof MinaPayloadHolder) {
            MinaPayloadHolder.unmarshal(exchange, (MinaPayloadHolder) payload);
        } else {
            exchange.getIn().setBody(payload);
        }
    }

    public static void setOut(Exchange exchange, Object payload) {
        if (payload instanceof MinaPayloadHolder) {
            MinaPayloadHolder.unmarshal(exchange, (MinaPayloadHolder) payload);
        } else {
            exchange.getOut().setBody(payload);
        }
    }

}
