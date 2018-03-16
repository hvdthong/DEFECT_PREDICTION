package org.apache.camel;

/**
 * Runtime version of the {@link org.apache.camel.InvalidTypeException}.
 *
 * @version $Revision: 659422 $
 */
public class InvalidPayloadRuntimeException extends RuntimeExchangeException {
    private final Class<?> type;

    public InvalidPayloadRuntimeException(Exchange exchange, Class<?> type) {
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
