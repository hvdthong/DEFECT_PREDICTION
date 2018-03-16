package org.apache.camel.component.file;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.IntrospectionSupport;

import java.io.File;
import java.util.Map;

/**
 *
 * @version $Revision: 523772 $
 */
public class FileComponent extends DefaultComponent<FileExchange> {
    public FileComponent() {
    }

    public FileComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint<FileExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        File file = new File(remaining);
        FileEndpoint result = new FileEndpoint(file, remaining, this);
        IntrospectionSupport.setProperties(result, parameters);
        return result;
    }
}
