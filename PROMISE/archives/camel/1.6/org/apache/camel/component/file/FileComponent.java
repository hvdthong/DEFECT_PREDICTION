package org.apache.camel.component.file;

import java.io.File;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * for working with file systems
 *
 * @version $Revision: 687736 $
 */
public class FileComponent extends DefaultComponent<FileExchange> {

    /**
     * Header key holding the value: the fixed filename to use for producing files.
     */
    public static final String HEADER_FILE_NAME = "org.apache.camel.file.name";

    /**
     * Header key holding the value: absolute filepath for the actual file produced (by file producer).
     * Value is set automatically by Camel
     */
    public static final String HEADER_FILE_NAME_PRODUCED = "org.apache.camel.file.name.produced";

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
