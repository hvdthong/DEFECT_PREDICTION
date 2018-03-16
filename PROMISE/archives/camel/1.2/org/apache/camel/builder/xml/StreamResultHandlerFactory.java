package org.apache.camel.builder.xml;

/**
 * @version $Revision: 1.1 $
 */
public class StreamResultHandlerFactory implements ResultHandlerFactory {
    public ResultHandler createResult() {
        return new StreamResultHandler();
    }
}
