package org.apache.camel.processor.validation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMResult;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A default error handler which just stores all the errors so they can be reported or transformed.
 *
 * @version $Revision: 684301 $
 */
public class DefaultValidationErrorHandler implements ValidatorErrorHandler {
    private static final transient Log LOG = LogFactory.getLog(DefaultValidationErrorHandler.class);
    private List<SAXParseException> warnings = new ArrayList<SAXParseException>();
    private List<SAXParseException> errors = new ArrayList<SAXParseException>();
    private List<SAXParseException> fatalErrors = new ArrayList<SAXParseException>();

    public void warning(SAXParseException e) throws SAXException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("warning: " + e, e);
        }
        warnings.add(e);
    }

    public void error(SAXParseException e) throws SAXException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("error: " + e, e);
        }
        errors.add(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fatalError: " + e, e);
        }
        fatalErrors.add(e);
    }

    public void reset() {
        warnings.clear();
        errors.clear();
        fatalErrors.clear();
    }

    public boolean isValid() {
        return errors.isEmpty() && fatalErrors.isEmpty();
    }

    public void handleErrors(Exchange exchange, Schema schema, DOMResult result) throws ValidationException {
        if (!isValid()) {
            throw new SchemaValidationException(exchange, schema, fatalErrors, errors, warnings);
        }
    }

    public void handleErrors(Exchange exchange, Object schema) throws ValidationException {
        if (!isValid()) {
            throw new SchemaValidationException(exchange, schema, fatalErrors, errors, warnings);
        }
    }
}
