package org.apache.camel.component.validator;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.processor.validation.ValidatingProcessor;

import org.springframework.core.io.Resource;

/**
 * Validator that uses Spring for reading the scheme resource.
 *
 * @version $Revision: 673477 $
 */
public class SpringValidator extends ValidatingProcessor {
    private Resource schemaResource;

    public Resource getSchemaResource() {
        return schemaResource;
    }

    public void setSchemaResource(Resource schemaResource) {
        this.schemaResource = schemaResource;
    }

    @Override
    protected Source createSchemaSource() throws IOException {
        if (schemaResource != null) {
            if (schemaResource.getURL() == null) {
                return new StreamSource(schemaResource.getInputStream());
            } else {
                return new StreamSource(schemaResource.getInputStream(), schemaResource.getURL().toExternalForm());
            }
        } else {
            throw new IllegalArgumentException("You must specify a schema, schemaFile, schemaResource, schemaSource or schemaUrl property");
        }
    }
}
