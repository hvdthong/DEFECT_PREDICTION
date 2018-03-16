package org.apache.camel.component.validator.jing;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.thaiopensource.relaxng.SchemaFactory;
import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.IncorrectSchemaException;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.Validator;
import com.thaiopensource.xml.sax.Jaxp11XMLReaderCreator;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.processor.validation.DefaultValidationErrorHandler;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.Resource;

/**
 * A validator which uses the <a
 * validate XML against RelaxNG
 * 
 * @version $Revision: 1.1 $
 */
public class JingValidator implements Processor {
    private static final transient Log LOG = LogFactory.getLog(JingValidator.class);
    private Schema schema;
    private SchemaFactory schemaFactory;
    private String schemaNamespace = XMLConstants.RELAXNG_NS_URI;
    private Resource schemaResource;
    private InputSource inputSource;
    private boolean compactSyntax;

    public void process(Exchange exchange) throws Exception {
        Jaxp11XMLReaderCreator xmlCreator = new Jaxp11XMLReaderCreator();
        DefaultValidationErrorHandler errorHandler = new DefaultValidationErrorHandler();

        PropertyMapBuilder mapBuilder = new PropertyMapBuilder();
        mapBuilder.put(ValidateProperty.XML_READER_CREATOR, xmlCreator);
        mapBuilder.put(ValidateProperty.ERROR_HANDLER, errorHandler);
        PropertyMap propertyMap = mapBuilder.toPropertyMap();

        Validator validator = getSchema().createValidator(propertyMap);

        Message in = exchange.getIn();
        SAXSource saxSource = in.getBody(SAXSource.class);
        if (saxSource == null) {
            Source source = ExchangeHelper.getMandatoryInBody(exchange, Source.class);
            saxSource = ExchangeHelper.convertToMandatoryType(exchange, SAXSource.class, source);
        }
        InputSource bodyInput = saxSource.getInputSource();

        XMLReader reader = xmlCreator.createXMLReader();
        reader.setContentHandler(validator.getContentHandler());
        reader.setDTDHandler(validator.getDTDHandler());
        reader.setErrorHandler(errorHandler);
        reader.parse(bodyInput);

        errorHandler.handleErrors(exchange, schema);
    }


    public Schema getSchema() throws IOException, IncorrectSchemaException, SAXException {
        if (schema == null) {
            SchemaFactory factory = getSchemaFactory();
            schema = factory.createSchema(getInputSource());
        }
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public InputSource getInputSource() throws IOException {
        if (inputSource == null) {
            Resource resource = getSchemaResource();
            if (resource == null) {
                throw new IllegalArgumentException("No schemaResource or inputSource specified");
            } else {
                InputStream inputStream = resource.getInputStream();
                if (inputStream == null) {
                    throw new IllegalArgumentException("No inputStream available for: " + resource);
                }
                inputSource = new InputSource(inputStream);
            }
        }
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public SchemaFactory getSchemaFactory() {
        if (schemaFactory == null) {
            schemaFactory = new SchemaFactory();
            schemaFactory.setCompactSyntax(compactSyntax);
            schemaFactory.setXMLReaderCreator(new Jaxp11XMLReaderCreator());
        }
        return schemaFactory;
    }

    public void setSchemaFactory(SchemaFactory schemaFactory) {
        this.schemaFactory = schemaFactory;
    }

    public Resource getSchemaResource() {
        return schemaResource;
    }

    public void setSchemaResource(Resource schemaResource) {
        this.schemaResource = schemaResource;
    }

    public String getSchemaNamespace() {
        return schemaNamespace;
    }

    public void setSchemaNamespace(String schemaNamespace) {
        this.schemaNamespace = schemaNamespace;
    }

    public boolean isCompactSyntax() {
        return compactSyntax;
    }

    public void setCompactSyntax(boolean compactSyntax) {
        this.compactSyntax = compactSyntax;
    }
}
