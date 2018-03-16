package org.apache.synapse.mediators.builtin;

import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.FaultHandler;
import org.apache.synapse.config.SynapseConfigUtils;
import org.apache.synapse.config.Entry;
import org.apache.synapse.mediators.AbstractListMediator;
import org.apache.synapse.mediators.MediatorProperty;
import org.jaxen.JaxenException;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
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
     * A Map containing features to be passed to the actual validator (Xerces)
     */
    private List explicityFeatures = new ArrayList();

    /**
     * This is the actual schema instance used to create a new schema
     * This is a thred-safe instance.
     */
    private Schema cachedSchema;

    /**
     * Lock used to ensure thread-safe creation and use of the above Validator
     */
    private final Object validatorLock = new Object();

    /**
     * The SchemaFactory for whcih used to create new schema instance
     */
    private  SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);


    public static final String DEFAULT_XPATH = "s11:Body/child::*[position()=1] | " +
        "s12:Body/child::*[position()=1]";
    
    public ValidateMediator() {
        try {
            this.source = new AXIOMXPath(DEFAULT_XPATH);
            this.source.addNamespace("s11", SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            this.source.addNamespace("s12", SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        } catch (JaxenException e) {
            String msg = "Error creating default source XPath expression : " + DEFAULT_XPATH;
            log.error(msg, e);
            throw new SynapseException(msg, e);
        }
    }

    public boolean mediate(MessageContext synCtx) {

        boolean traceOn = isTraceOn(synCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Start : Validate mediator");

            if (traceOn && trace.isTraceEnabled()) {
                trace.trace("Message : " + synCtx.getEnvelope());
            }
        }

        Source validateSrc = getValidationSource(synCtx, traceOrDebugOn, traceOn);

        boolean reCreate = false;
        for (Iterator iter = schemaKeys.iterator(); iter.hasNext();) {
            String propKey = (String) iter.next();
            Entry dp = synCtx.getConfiguration().getEntryDefinition(propKey);
            if (dp != null && dp.isDynamic()) {
                if (!dp.isCached() || dp.isExpired()) {
                }
            }
        }

        MyErrorHandler errorHandler = new MyErrorHandler();

        synchronized (validatorLock) {
            if (reCreate || cachedSchema == null) {

                factory.setErrorHandler(errorHandler);
                StreamSource[] sources = new StreamSource[schemaKeys.size()];
                int i = 0;
                for (Iterator iterator = schemaKeys.iterator(); iterator.hasNext();) {
                    String propName = (String) iterator.next();
                    sources[i++] = SynapseConfigUtils.getStreamSource(synCtx.getEntry(propName));
                }

                try {
                    cachedSchema = factory.newSchema(sources);
                } catch (SAXException e) {
                    handleException("Error creating a new schema objects for " +
                        "schemas : " + schemaKeys.toString(), e, synCtx);
                }

                if (errorHandler.isValidationError()) {
                    errorHandler.setValidationError(false);

                    if (traceOrDebugOn) {
                        traceOrDebug(traceOn, "Error creating a new schema objects for " +
                            "schemas : " + schemaKeys.toString());
                    }
                }
            }
        }

        try {
            Validator validator = cachedSchema.newValidator();
            validator.setErrorHandler(errorHandler);

            validator.validate(validateSrc);

            if (errorHandler.isValidationError()) {

                if (traceOrDebugOn) {
                    String msg = "Validation of element returned by XPath : " + source +
                        " failed against the given schema(s) " + schemaKeys +
                        "with error : " + errorHandler.getSaxParseException().getMessage() +
                        " Executing 'on-fail' sequence";
                    traceOrDebug(traceOn, msg);

                    synCtx.getServiceLog().warn(msg);

                    if (traceOn && trace.isTraceEnabled()) {
                        log.debug("Failed message envelope : " + synCtx.getEnvelope());
                    }
                }

                synCtx.setProperty(SynapseConstants.ERROR_MESSAGE,
                    errorHandler.getSaxParseException().getMessage());
                synCtx.setProperty(SynapseConstants.ERROR_DETAIL,
                    FaultHandler.getStackTrace(errorHandler.getSaxParseException()));

                return super.mediate(synCtx);
            }
        } catch (SAXException e) {
            handleException("Error validating " + source + " element", e, synCtx);
        } catch (IOException e) {
            handleException("Error validating " + source + " element", e, synCtx);
        }

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Validation of element returned by the XPath expression : "
                + source + " succeeded against the given schemas and the current message");
        }

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "End : Validate mediator");
        }

        return true;
    }

    /**
     * Get the validation Source for the message context
     *
     * @param synCtx the current message to validate
     * @param traceOrDebugOn is tracing or debugging on?
     * @param traceOn is tracing on?
     * @return the validation Source for the current message
     */
    private Source getValidationSource(MessageContext synCtx,
        boolean traceOrDebugOn, boolean traceOn) {

        try {
            ByteArrayOutputStream baosForSource = new ByteArrayOutputStream();
            XMLStreamWriter xsWriterForSource =
                    XMLOutputFactory.newInstance().createXMLStreamWriter(baosForSource);

            OMNode validateSource = getValidateSource(synCtx);
            if (traceOrDebugOn) {
                traceOrDebug(traceOn, "Validation source : " + validateSource.toString());
            }
            validateSource.serialize(xsWriterForSource);

            ByteArrayInputStream baisFromSource = new ByteArrayInputStream(
                baosForSource.toByteArray());
            XMLReader reader = XMLReaderFactory.createXMLReader();
            return new SAXSource(reader, new InputSource(baisFromSource));

        } catch (Exception e) {
            handleException("Error accessing source element : " + source, e, synCtx);
        }
    }

    /**
     * This class handles validation errors to be used for the error reporting
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
         * @param validationError  is occur validation error?
         */
        public void setValidationError(boolean validationError) {
            this.validationError = validationError;
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
                    + source + " did not result in an OMNode : " + o, synCtx);
            }
        } catch (JaxenException e) {
            handleException("Error evaluating XPath expression : " + source, e, synCtx);
        }
        return null;
    }


    /**
     * Get a mediator feature. The common use case is a feature for the
     * underlying Xerces validator
     *
     * @param key property key / feature name
     * @return property string value (usually true|false)
     */
    public Object getFeature(String key) {
        for (Iterator iter = explicityFeatures.iterator(); iter.hasNext();) {
            MediatorProperty prop = (MediatorProperty) iter.next();
            if (key.equals(prop.getName())) {
                return prop.getValue();
            }
        }
        return null;
    }

    /**
     * add a feature which need to set for the Schema Factory
     *
     * @param  featureName The name of the feature
     * @param isFeatureEnable should this feature enable?(true|false)
     * @see #getFeature(String)
     * @throws SAXException on an unknown feature
     */
   public void addFeature(String featureName, boolean isFeatureEnable) throws SAXException {
        MediatorProperty mp = new MediatorProperty();
        mp.setName(featureName);
        if (isFeatureEnable) {
            mp.setValue("true");
        } else {
            mp.setValue("false");
        }
        explicityFeatures.add(mp);
        factory.setFeature(featureName, isFeatureEnable);
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
     * Features for the actual Xerces validator
     * @return explicityFeatures to be passed to the Xerces validator
     */
    public List getFeatures() {
        return explicityFeatures;
    }
}
