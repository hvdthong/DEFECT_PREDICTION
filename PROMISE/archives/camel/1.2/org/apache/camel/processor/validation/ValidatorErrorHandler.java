package org.apache.camel.processor.validation;

import javax.xml.transform.dom.DOMResult;
import javax.xml.validation.Schema;

import org.xml.sax.ErrorHandler;

import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;

/**
 * @version $Revision: $
 */
public interface ValidatorErrorHandler extends ErrorHandler {
    /**
     * Resets any state within this error handler
     */
    void reset();

    /**
     * Process any errors which may have occurred during validation
     *
     * @param exchange
     * @param schema
     * @param result
     */
    void handleErrors(Exchange exchange, Schema schema, DOMResult result) throws ValidationException;
}
