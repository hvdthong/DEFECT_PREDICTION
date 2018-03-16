package org.apache.camel;

/**
 * @version $Revision: 1.1 $
 */
public class InvalidPayloadException extends CamelExchangeException {
    private final Class<?> type;

    public InvalidPayloadException(Exchange exchange, Class<?> type) {
        super("No in body available of type: " + type.getName()
              + NoSuchPropertyException.valueDescription(exchange.getIn().getBody()), exchange);
        this.type = type;
    }

    /**
     * The expected type of the body
     */
    public Class<?> getType() {
        return type;
    }
}
