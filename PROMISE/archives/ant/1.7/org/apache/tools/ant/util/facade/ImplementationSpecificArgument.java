package org.apache.tools.ant.util.facade;

import org.apache.tools.ant.types.Commandline;

/**
 * Extension of Commandline.Argument with a new attribute that choses
 * a specific implementation of the facade.
 *
 *
 * @since Ant 1.5
 */
public class ImplementationSpecificArgument extends Commandline.Argument {
    private String impl;

    /** Constructor for ImplementationSpecificArgument. */
    public ImplementationSpecificArgument() {
        super();
    }

    /**
     * Set the implementation this argument is for.
     * @param impl the implementation this command line argument is for.
     */
    public void setImplementation(String impl) {
        this.impl = impl;
    }

    /**
     * Return the parts this Argument consists of, if the
     * implementation matches the chosen implementation.
     * @see org.apache.tools.ant.types.Commandline.Argument#getParts()
     * @param chosenImpl the implementation to check against.
     * @return the parts if the implemention matches or an zero length
     *         array if not.
     */
    public final String[] getParts(String chosenImpl) {
        if (impl == null || impl.equals(chosenImpl)) {
            return super.getParts();
        } else {
            return new String[0];
        }
    }
}
