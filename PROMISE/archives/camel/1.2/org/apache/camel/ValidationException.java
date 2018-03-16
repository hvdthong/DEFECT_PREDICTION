package org.apache.camel;

/**
 * The base class for any validation exception, such as
 * {@link org.apache.camel.processor.validation.SchemaValidationException} so
 * that it is easy to treat all validation errors in a similar way irrespective
 * of the particular validation technology used.
 * 
 * @version $Revision: $
 */
public class ValidationException extends CamelExchangeException {

    public ValidationException(Exchange exchange, String message) {
        super(message, exchange);
    }

}
