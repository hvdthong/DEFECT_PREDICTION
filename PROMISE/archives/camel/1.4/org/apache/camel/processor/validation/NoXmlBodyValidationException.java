package org.apache.camel.processor.validation;

import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;

/**
 * An exception found if no XML body is available on the inbound message
 *
 * @version $Revision: 630591 $
 */
public class NoXmlBodyValidationException extends ValidationException {

    public NoXmlBodyValidationException(Exchange exchange) {
        super(exchange, "No XML body could be found on the input message" + exchange);
    }
}
