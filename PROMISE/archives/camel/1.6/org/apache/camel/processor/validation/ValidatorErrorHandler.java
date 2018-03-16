package org.apache.camel.processor.validation;

import javax.xml.transform.dom.DOMResult;
import javax.xml.validation.Schema;

import org.xml.sax.ErrorHandler;

import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;

/**
 * Validator error handler.
 *
 * @version $Revision: 659760 $
 */
public interface ValidatorErrorHandler extends ErrorHandler {

    /**
     * Resets any state within this error handler
     */
    void reset();

    /**
     * Process any errors which may have occurred during validation
     *
     * @param exchange the exchange
     * @param schema   the schema
     * @param result   the result
     */
    void handleErrors(Exchange exchange, Schema schema, DOMResult result) throws ValidationException;
}
