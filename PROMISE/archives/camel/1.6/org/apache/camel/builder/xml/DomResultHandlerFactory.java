package org.apache.camel.builder.xml;

/**
 * Factory for {@link DomResultHandler}
 *
 * @version $Revision: 660275 $
 */
public class DomResultHandlerFactory implements ResultHandlerFactory {

    public ResultHandler createResult() {
        return new DomResultHandler();
    }
}
