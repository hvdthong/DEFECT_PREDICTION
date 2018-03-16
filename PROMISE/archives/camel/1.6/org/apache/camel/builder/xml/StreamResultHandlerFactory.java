package org.apache.camel.builder.xml;

/**
 * Factory for {@link StreamResultHandler}
 *
 * @version $Revision: 660275 $
 */
public class StreamResultHandlerFactory implements ResultHandlerFactory {

    public ResultHandler createResult() {
        return new StreamResultHandler();
    }
}
