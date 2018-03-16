package org.apache.camel.component.validator.msv;

import java.util.Map;

import javax.xml.XMLConstants;

import org.apache.camel.component.validator.SpringValidator;
import org.apache.camel.component.validator.ValidatorComponent;

import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.jaxp.validation.RELAXNGSchemaFactoryImpl;

/**
 *
 * @version $Revision: 630574 $
 */
public class MsvComponent extends ValidatorComponent {
    private RELAXNGSchemaFactoryImpl schemaFactory;

    public RELAXNGSchemaFactoryImpl getSchemaFactory() throws VerifierConfigurationException {
        if (schemaFactory == null) {
            schemaFactory = new RELAXNGSchemaFactoryImpl();
        }
        return schemaFactory;
    }

    public void setSchemaFactory(RELAXNGSchemaFactoryImpl schemaFactory) {
        this.schemaFactory = schemaFactory;
    }

    protected void configureValidator(SpringValidator validator, String uri, String remaining, Map parameters) throws Exception {
        validator.setSchemaLanguage(XMLConstants.RELAXNG_NS_URI);
        validator.setSchemaFactory(getSchemaFactory());
        super.configureValidator(validator, uri, remaining, parameters);
    }
}
