package org.apache.camel.component.mina;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;

/**
 * Holder object for sending an exchange over the wire using the MINA ObjectSerializationCodecFactory codec.
 * This is configured using the <tt>transferExchange=true</tt> option for the TCP protocol.
 * <p/>
 * As opposed to normal usage of camel-mina where only the body part of the exchange is transfered, this holder
 * object serializes the following fields over the wire:
 * <ul>
 *     <li>in body</li>
 *     <li>out body</li>
 *     <li>in headers</li>
 *     <li>out headers</li>
 *     <li>exchange properties</li>
 *     <li>exception</li>
 * </ul>
 *
 * @version $Revision: 641198 $
 */
public class MinaPayloadHolder implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object inBody;
    private Object outBody;
    private Map<String, Object> inHeaders = new LinkedHashMap<String, Object>();
    private Map<String, Object> outHeaders = new LinkedHashMap<String, Object>();
    private Map<String, Object> properties = new LinkedHashMap<String, Object>();
    private Throwable exception;

    /**
     * Creates a payload object with the information from the given exchange.
     *
     * @param exchange     the exchange
     * @return the holder object with information copied form the exchange
     */
    public static MinaPayloadHolder marshal(Exchange exchange) {
        MinaPayloadHolder payload = new MinaPayloadHolder();

        payload.inBody = exchange.getIn().getBody();
        if (exchange.getOut(false) != null) {
            payload.outBody = exchange.getOut().getBody();
        }
        payload.inHeaders.putAll(exchange.getIn().getHeaders());
        payload.outHeaders.putAll(exchange.getOut().getHeaders());
        payload.properties.putAll(exchange.getProperties());
        payload.exception = exchange.getException();

        return payload;
    }

    /**
     * Transfers the information from the payload to the exchange.
     *
     * @param exchange   the exchange to set values from the payload
     * @param payload    the payload with the values
     */
    public static void unmarshal(Exchange exchange, MinaPayloadHolder payload) {
        exchange.getIn().setBody(payload.inBody);
        exchange.getOut().setBody(payload.outBody);
        exchange.getIn().setHeaders(payload.inHeaders);
        exchange.getOut().setHeaders(payload.outHeaders);
        for (String key : payload.properties.keySet()) {
            exchange.setProperty(key, payload.properties.get(key));
        }
        exchange.setException(payload.exception);
    }

    public String toString() {
        return "MinaPayloadHolder{" + "inBody=" + inBody + ", outBody=" + outBody + ", inHeaders="
               + inHeaders + ", outHeaders=" + outHeaders + ", properties=" + properties + ", exception="
               + exception + '}';
    }

}
