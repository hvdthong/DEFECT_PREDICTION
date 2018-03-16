package org.apache.camel.builder.xml;

/**
 * Factory for {@link StringResultHandler}
 *
 * @version $Revision: 659760 $
 */
public class StringResultHandlerFactory implements ResultHandlerFactory {

    public ResultHandler createResult() {
        return new StringResultHandler();
    }
}
