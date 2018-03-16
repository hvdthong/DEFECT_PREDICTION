package org.apache.synapse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private static final Log log = LogFactory.getLog(FaultHandler.class);
    private static final Log trace = LogFactory.getLog(SynapseConstants.TRACE_LOGGER);

    public void handleFault(MessageContext synCtx) {

        boolean traceOn = synCtx.getTracingState() == SynapseConstants.TRACING_ON;
        boolean traceOrDebugOn = traceOn || log.isDebugEnabled();

        if (traceOrDebugOn) {
            traceOrDebugWarn(traceOn, "FaultHandler executing impl: " + this.getClass().getName());
        }

        try {
            synCtx.getServiceLog().info("FaultHandler executing impl: " + this.getClass().getName());
            onFault(synCtx);

        } catch (SynapseException e) {

            Stack faultStack = synCtx.getFaultStack();
            if (faultStack != null && !faultStack.isEmpty()) {
                ((FaultHandler) faultStack.pop()).handleFault(synCtx);
            }
        }
    }

    /**
     * Extract and set ERROR_MESSAGE and ERROR_DETAIL to the message context from the Exception
     * @param synCtx the message context
     * @param e the exception encountered
     */
    public void handleFault(MessageContext synCtx, Exception e) {

        boolean traceOn = synCtx.getTracingState() == SynapseConstants.TRACING_ON;
        boolean traceOrDebugOn = traceOn || log.isDebugEnabled();

        if (synCtx.getProperty(SynapseConstants.ERROR_CODE) == null) {
            synCtx.setProperty(SynapseConstants.ERROR_CODE, "00000");
        }
        if (synCtx.getProperty(SynapseConstants.ERROR_MESSAGE) == null) {
            synCtx.setProperty(SynapseConstants.ERROR_MESSAGE, e.getMessage().split("\n")[0]);
        }
        synCtx.setProperty(SynapseConstants.ERROR_DETAIL, getStackTrace(e));

        if (traceOrDebugOn) {
            traceOrDebugWarn(traceOn, "Fault handler - setting ERROR_MESSAGE : " +
                synCtx.getProperty(SynapseConstants.ERROR_MESSAGE));
            traceOrDebugWarn(traceOn, "Fault handler - setting ERROR_DETAIL : " +
                synCtx.getProperty(SynapseConstants.ERROR_DETAIL));
        }

        synCtx.getServiceLog().warn("Fault handler - setting ERROR_MESSAGE : " +
            synCtx.getProperty(SynapseConstants.ERROR_MESSAGE));

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

    /**
     * Get the stack trace into a String
     * @param aThrowable
     * @return the stack trace as a string
     */
    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    private void traceOrDebugWarn(boolean traceOn, String msg) {
        if (traceOn) {
            trace.warn(msg);
        }
        log.warn(msg);
    }

}
