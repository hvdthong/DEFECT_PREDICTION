package org.apache.synapse;

/**
 * <p>Runtime exception for Synapse code to throw
 */
public class SynapseException extends RuntimeException {

    private static final long serialVersionUID = -7244032125641596311L;

    public SynapseException(String string) {
        super(string);
    }

    public SynapseException(String msg, Throwable e) {
        super(msg, e);
    }

    public SynapseException(Throwable t) {
        super(t);
    }

}
