package org.apache.camel;

/**
 * Is thrown if the payload from the exchange could not be retrieve because of being null, wrong class type etc.
 *
 * @version $Revision: 659422 $
 */
public class InvalidPayloadException extends CamelExchangeException {
    private final Class<?> type;

    public InvalidPayloadException(Exchange exchange, Class<?> type) {
        this(exchange, type, exchange.getIn());
    }

    public InvalidPayloadException(Exchange exchange, Class<?> type, Message message) {
        super("No in body available of type: " + type.getName()
              + NoSuchPropertyException.valueDescription(message.getBody()) + " on: " + message, exchange);
        this.type = type;
    }

    /**
     * The expected type of the body
     */
    public Class<?> getType() {
        return type;
    }
}
