package org.apache.camel.builder.xml;

/**
 * @version $Revision: 1.1 $
 */
public class DomResultHandlerFactory implements ResultHandlerFactory {
    public ResultHandler createResult() {
        return new DomResultHandler();
    }
}
