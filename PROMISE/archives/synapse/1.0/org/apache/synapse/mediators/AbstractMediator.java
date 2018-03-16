package org.apache.synapse.mediators;

import org.apache.synapse.Constants;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;

/**
 * This class is an abstract Mediator, that defines the logging and debugging
 * elements of a mediator class.
 */
public abstract class AbstractMediator implements Mediator {

    /** The parent tracing state */
     protected  int parentTraceState = Constants.TRACING_UNSET;

    /** State of tracing for the current mediator */
     protected int traceState = Constants.TRACING_UNSET;

    /**
     * Returns the class name of the mediator
     *
     * @return the class name of the mediator
     */
    public String getType() {
        String cls = getClass().getName();
        int p = cls.lastIndexOf(".");
        if (p == -1)
            return cls;
        else
            return cls.substring(p + 1);
    }

    /**
     * Returns the tracing state
     *
     * @return int
     */
    public int getTraceState() {
        return traceState;
    }

    /**
     * Set the tracing state variable
     *
     * @param traceState
     */
    public void setTraceState(int traceState) {
        this.traceState = traceState;
    }

    /**
     * This method is used to save previous tracing state and set next the tracing state for a child
     * mediator
     *
     * @param synCtx current message
     */
    public void saveAndSetTraceState(MessageContext synCtx) {
        parentTraceState = synCtx.getTracingState();
        synCtx.setTracingState(traceState);
    }

    /**
     * This method is used to restore parent tracing state back
     * @param synCtx the current message
     */
    public void restoreTracingState(MessageContext synCtx){
        synCtx.setTracingState(parentTraceState);
    }

    /**
     * Should this mediator perform tracing? True if its explicitly asked to
     * trace, or its parent has been asked to trace and it does not reject it
     * @param parentTraceState parents trace state
     * @return true if tracing should be performed
     */
    public boolean shouldTrace(int parentTraceState){
        return (traceState == Constants.TRACING_ON) ||
                (traceState == Constants.TRACING_UNSET &&
                parentTraceState == Constants.TRACING_ON);
    }
}
