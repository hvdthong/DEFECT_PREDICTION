package org.apache.synapse.mediators.builtin;

import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Constants;
import org.apache.synapse.config.Util;
import org.apache.synapse.config.Entry;
import org.apache.synapse.mediators.AbstractListMediator;
import org.apache.synapse.mediators.MediatorProperty;
import org.jaxen.JaxenException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Validate a message or an element against a schema
 * <p/>
 * This internally uses the Xerces2-j parser, which cautions a lot about thread-safety and
 * memory leaks. Hence this initial implementation will create a single parser instance
 * for each unique mediator instance, and re-use it to validate multiple messages - even
 * concurrently - by synchronizing access
 */
public class ValidateMediator extends AbstractListMediator {

    private static final Log log = LogFactory.getLog(ValidateMediator.class);
    private static final Log trace = LogFactory.getLog(Constants.TRACE_LOGGER);

    /**
     */

    /**
     * A list of property keys, referring to the schemas to be used for the validation
     */
    private List schemaKeys = new ArrayList();

    /**
     * An XPath expression to be evaluated against the message to find the element to be validated.
     * If this is not specified, the validation will occur against the first child element of the
     * SOAP body
     */
    private AXIOMXPath source = null;

    /**
     * A Map containing properties for the validate mediator - such as
     * features to be passed to the actual validator (Xerces)
     */
    private List properties = new ArrayList();

    /**
     * This is the actual Validator instance used to validate messages - probably
     * by multiple threads. Always *USE* validatorLock to synchronize access to this
     */
    private Validator validator = null;

    /**
     * Lock used to ensure thread-safe creation and use of the above Validator
     */
    private final Object validatorLock = new Object();

    /**
     * This is the reference to the DefaultHandler instance
     */
    private final MyErrorHandler errorHandler = new MyErrorHandler();


    public ValidateMediator() {
        try {
            this.source = new AXIOMXPath(DEFAULT_XPATH);
            this.source.addNamespace("s11", SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            this.source.addNamespace("s12", SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        } catch (JaxenException e) {
            handleException("Error creating source XPath expression", e);
        }
    }

    /**
     * Return the OMNode to be validated. If a source XPath is not specified, this will
     *
     * @param synCtx the message context
     * @return the OMNode against which validation should be performed
     */
    private OMNode getValidateSource(MessageContext synCtx) {

        try {
            Object o = source.evaluate(synCtx.getEnvelope());
            if (o instanceof OMNode) {
                return (OMNode) o;
            } else if (o instanceof List && !((List) o).isEmpty()) {
            } else {
                handleException("The evaluation of the XPath expression "
                    + source + " must result in an OMNode");
            }
        } catch (JaxenException e) {
            handleException("Error evaluating XPath " + source + " on message");
        }
        return null;
    }

    public boolean mediate(MessageContext synCtx) {

        log.debug("ValidateMediator - Validate mediator mediate()");
        ByteArrayInputStream baisFromSource = null;
        boolean shouldTrace = shouldTrace(synCtx.getTracingState());
        if (shouldTrace) {
            trace.trace("Start : Validate mediator");
        }
        try {
            ByteArrayOutputStream baosForSource = new ByteArrayOutputStream();
            XMLStreamWriter xsWriterForSource =
                XMLOutputFactory.newInstance().createXMLStreamWriter(baosForSource);

            OMNode validateSource = getValidateSource(synCtx);
            if (shouldTrace) {
                trace.trace("Validate Source : " + validateSource.toString());
            }
            validateSource.serialize(xsWriterForSource);
            baisFromSource = new ByteArrayInputStream(baosForSource.toByteArray());

        } catch (Exception e) {
            handleException("Error accessing source element for validation : " + source, e);
        }

        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            SAXSource saxSrc = new SAXSource(reader, new InputSource(baisFromSource));

            synchronized (validatorLock) {

                initialize(synCtx);

                validator.validate(saxSrc);

                if (errorHandler.isValidationError()) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                            "Validation of element returned by XPath : " + source +
                                " failed against the given schemas with Message : " +
                                errorHandler.getSaxParseException().getMessage() +
                                " Executing 'on-fail' sequence");
                        log.debug("Failed message envelope : " + synCtx.getEnvelope());
                    }
                    if (shouldTrace) {
                        trace.trace("Validation failed. Invoking the \"on-fail\" " +
                            "sequence of mediators");
                    }
                    return super.mediate(synCtx);
                }
            }
        } catch (SAXException e) {
            handleException("Error validating " + source + " element" + e.getMessage(), e);
        } catch (IOException e) {
            handleException("Error validating " + source + " element" + e.getMessage(), e);
        }

        log.debug("validation of element returned by the XPath expression : " + source +
            " succeeded against the given schemas and the current message");
        if (shouldTrace) {
            trace.trace("End : Validate mediator");
        }
        return true;
    }

    /**
     * Perform actual initialization of this validate mediator instance - if required
     */
    private void initialize(MessageContext msgCtx) {

        boolean reCreate = false;

        Iterator iter = schemaKeys.iterator();
        while (iter.hasNext()) {
            String propKey = (String) iter.next();
            Entry dp = msgCtx.getConfiguration().getEntryDefinition(propKey);
            if (dp != null && dp.isDynamic()) {
                if (!dp.isCached() || dp.isExpired()) {
                }
            }
        }

        if (!reCreate && validator != null) {
            return;
        }

        try {
            SchemaFactory factory = SchemaFactory.newInstance(DEFAULT_SCHEMA_LANGUAGE);
            errorHandler.setValidationError(false);
            factory.setErrorHandler(errorHandler);

            iter = properties.iterator();
            while (iter.hasNext()) {
                MediatorProperty prop = (MediatorProperty) iter.next();
                factory.setFeature(
                    prop.getName(), prop.getValue() != null && "true".equals(prop.getValue()));
            }

            Schema schema = null;

            StreamSource[] sources = new StreamSource[schemaKeys.size()];
            iter = schemaKeys.iterator();
            int i = 0;
            while (iter.hasNext()) {
                String propName = (String) iter.next();
                sources[i++] = Util.getStreamSource(msgCtx.getEntry(propName));
            }
            schema = factory.newSchema(sources);

            validator = schema.newValidator();
            validator.setErrorHandler(errorHandler);

        } catch (SAXException e) {
            handleException("Error creating Validator", e);
        }
    }

    /**
     * This class handles validation errors to be used for error reporting
     */
    private class MyErrorHandler extends DefaultHandler {

        private boolean validationError = false;
        private SAXParseException saxParseException = null;

        public void error(SAXParseException exception) throws SAXException {
            validationError = true;
            saxParseException = exception;
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            validationError = true;
            saxParseException = exception;
        }

        public void warning(SAXParseException exception) throws SAXException {
        }

        public boolean isValidationError() {
            return validationError;
        }

        public SAXParseException getSaxParseException() {
            return saxParseException;
        }

        /**
         * To set explicitly validation error condition 
         * @param validationError
         */
        public void setValidationError(boolean validationError) {
            this.validationError = validationError;
        }
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

    private void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }


    /**
     * Get a mediator property. The common use case is a feature for the
     * underlying Xerces validator
     *
     * @param key property key / feature name
     * @return property string value (usually true|false)
     */
    public Object getProperty(String key) {
        Iterator iter = properties.iterator();
        while (iter.hasNext()) {
            MediatorProperty prop = (MediatorProperty) iter.next();
            if (key.equals(prop.getName())) {
                return prop.getValue();
            }
        }
        return null;
    }

    /**
     * Set a property for this mediator
     *
     * @param key   the property key / feature name
     * @param value property string value (usually true|false)
     * @see #getProperty(String)
     */
    public void setProperty(String key, Object value) {
        MediatorProperty prop = new MediatorProperty();
        prop.setName(key);
        prop.setValue(value.toString());
        properties.add(prop);
    }

    /**
     * Add a list of 'MediatorProperty'ies to this mediator
     *
     * @param list a List of MediatorProperty objects
     */
    public void addAllProperties(List list) {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof MediatorProperty) {
                MediatorProperty prop = (MediatorProperty) o;
                setProperty(prop.getName(), prop.getValue());
            } else {
                handleException("Attempt to set invalid property type. " +
                    "Expected MediatorProperty type got " + o.getClass().getName());
            }
        }
    }

    /**
     * Set a list of local property names which refer to a list of schemas to be
     * used for validation
     *
     * @param schemaKeys list of local property names
     */
    public void setSchemaKeys(List schemaKeys) {
        this.schemaKeys = schemaKeys;
    }

    /**
     * Set the given XPath as the source XPath
     * @param source an XPath to be set as the source
     */
    public void setSource(AXIOMXPath source) {
       this.source = source;
    }

    /**
     * Get the source XPath which yeilds the source element for validation
     * @return the XPath which yeilds the source element for validation
     */
    public AXIOMXPath getSource() {
        return source;
    }

    /**
     * The keys for the schema resources used for validation
     * @return schema registry keys
     */
    public List getSchemaKeys() {
        return schemaKeys;
    }

    /**
     * Properties for the actual Xerces validator
     * @return properties to be passed to the Xerces validator
     */
    public List getProperties() {
        return properties;
    }
}
