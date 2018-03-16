package org.apache.camel.builder.script;

import org.apache.camel.RuntimeCamelException;

/**
 * An exception thrown if the script evaluation fails
 * 
 * @version $Revision: 563665 $
 */
public class ScriptEvaluationException extends RuntimeCamelException {

    public ScriptEvaluationException(String message) {
        super(message);
    }

    public ScriptEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptEvaluationException(Throwable cause) {
        super(cause);
    }
}
