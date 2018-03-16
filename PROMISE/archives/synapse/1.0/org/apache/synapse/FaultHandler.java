package org.apache.synapse;

import java.util.Stack;
import java.io.StringWriter;
import java.io.Writer;
import java.io.PrintWriter;

/**
 * This is an abstract class that handles an unexpected error during Synapse mediation, but looking
 * at the stack of registered FaultHanders and invoking on them as appropriate. Sequences and
 * Endpoints would be Synapse entities that handles faults. If such an entity is unable to handle
 * an error condition, then a SynapseException should be thrown, which triggers this fault
 * handling logic.
 */
public abstract class FaultHandler {

    public void handleFault(MessageContext synCtx) {

        try {
            onFault(synCtx);

        } catch (SynapseException e) {

            Stack faultStack = synCtx.getFaultStack();
            if (faultStack != null && !faultStack.isEmpty()) {
                ((FaultHandler) faultStack.pop()).handleFault(synCtx);
            }
        }
    }

    public void handleFault(MessageContext synCtx, Exception e) {

        if (synCtx.getProperty(Constants.ERROR_CODE) == null) {
            synCtx.setProperty(Constants.ERROR_CODE, "00000");
        }
        if (synCtx.getProperty(Constants.ERROR_MESSAGE) == null) {
            synCtx.setProperty(Constants.ERROR_MESSAGE, e.getMessage().split("\n")[0]);
        }
        synCtx.setProperty(Constants.ERROR_DETAIL, getStackTrace(e));

        try {
            onFault(synCtx);

        } catch (SynapseException se) {

            Stack faultStack = synCtx.getFaultStack();
            if (faultStack != null && !faultStack.isEmpty()) {
                ((FaultHandler) faultStack.pop()).handleFault(synCtx, se);
            }
        }
    }

    /**
     * This will be executed to handle any Exceptions occured within the Synapse environment.
     * @param synCtx SynapseMessageContext of which the fault occured message comprises
     * @throws SynapseException in case there is a failure in the fault execution
     */
    public abstract void onFault(MessageContext synCtx);

    private static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }
}
