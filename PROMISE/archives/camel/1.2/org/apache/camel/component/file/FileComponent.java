package org.apache.camel.component.file;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.io.File;
import java.util.Map;

/**
 * for working with file systems
 * 
 * @version $Revision: 523772 $
 */
public class FileComponent extends DefaultComponent<FileExchange> {
    public static final String HEADER_FILE_NAME = "org.apache.camel.file.name";

    public FileComponent() {
    }

    public FileComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint<FileExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        File file = new File(remaining);
        FileEndpoint result = new FileEndpoint(file, uri, this);
        setProperties(result, parameters);
        return result;
    }
}
