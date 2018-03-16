package org.apache.camel;

/**
 * Thrown if the body could not be converted to the required type
 * 
 * @version $Revision: 1.1 $
 */
public class ExpectedBodyTypeException extends RuntimeCamelException {
    private final Exchange exchange;
    private final Class expectedBodyType;

    public ExpectedBodyTypeException(Exchange exchange, Class expectedBodyType) {
        super("Could not extract IN message body as type: " + expectedBodyType + " body is: "
              + exchange.getIn().getBody());
        this.exchange = exchange;
        this.expectedBodyType = expectedBodyType;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public Class getExpectedBodyType() {
        return expectedBodyType;
    }
}
