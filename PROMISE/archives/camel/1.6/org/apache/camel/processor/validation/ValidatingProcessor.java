package org.apache.camel.processor.validation;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A processor which validates the XML version of the inbound message body
 * against some schema either in XSD or RelaxNG
 * 
 * @version $Revision: 684301 $
 */
public class ValidatingProcessor implements Processor {
    private String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    private Schema schema;
    private Source schemaSource;
    private SchemaFactory schemaFactory;
    private URL schemaUrl;
    private File schemaFile;
    private ValidatorErrorHandler errorHandler = new DefaultValidationErrorHandler();

    public void process(Exchange exchange) throws Exception {
        Schema schema = getSchema();
        Validator validator = schema.newValidator();

        Source source = exchange.getIn().getBody(DOMSource.class);
        if (source == null) {
            throw new NoXmlBodyValidationException(exchange);
        }

        ValidatorErrorHandler handler = errorHandler.getClass().newInstance();
        validator.setErrorHandler(handler);

        DOMResult result = new DOMResult();
        validator.validate(source, result);

        handler.handleErrors(exchange, schema, result);
    }


    public Schema getSchema() throws IOException, SAXException {
        if (schema == null) {
            schema = createSchema();
        }
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getSchemaLanguage() {
        return schemaLanguage;
    }

    public void setSchemaLanguage(String schemaLanguage) {
        this.schemaLanguage = schemaLanguage;
    }

    public Source getSchemaSource() throws IOException {
        if (schemaSource == null) {
            schemaSource = createSchemaSource();
        }
        return schemaSource;
    }

    public void setSchemaSource(Source schemaSource) {
        this.schemaSource = schemaSource;
    }

    public URL getSchemaUrl() {
        return schemaUrl;
    }

    public void setSchemaUrl(URL schemaUrl) {
        this.schemaUrl = schemaUrl;
    }

    public File getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(File schemaFile) {
        this.schemaFile = schemaFile;
    }

    public SchemaFactory getSchemaFactory() {
        if (schemaFactory == null) {
            schemaFactory = createSchemaFactory();
        }
        return schemaFactory;
    }

    public void setSchemaFactory(SchemaFactory schemaFactory) {
        this.schemaFactory = schemaFactory;
    }

    public ValidatorErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(ValidatorErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }


    protected SchemaFactory createSchemaFactory() {
        return SchemaFactory.newInstance(schemaLanguage);
    }

    protected Source createSchemaSource() throws IOException {
        throw new IllegalArgumentException("You must specify a schema, "
                                           + "schemaFile, schemaSource or schemaUrl property");
    }

    protected Schema createSchema() throws SAXException, IOException {
        SchemaFactory factory = getSchemaFactory();

        URL url = getSchemaUrl();
        if (url != null) {
            return factory.newSchema(url);
        }
        File file = getSchemaFile();
        if (file != null) {
            return factory.newSchema(file);
        }
        return factory.newSchema(getSchemaSource());
    }

}
