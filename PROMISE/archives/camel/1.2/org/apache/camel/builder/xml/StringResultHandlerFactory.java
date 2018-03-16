package org.apache.camel.builder.xml;

/**
 * @version $Revision: 1.1 $
 */
public class StringResultHandlerFactory implements ResultHandlerFactory {

    public ResultHandler createResult() {
        return new StringResultHandler();
    }
}
