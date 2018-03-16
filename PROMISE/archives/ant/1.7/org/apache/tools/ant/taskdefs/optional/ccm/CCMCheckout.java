package org.apache.tools.ant.taskdefs.optional.ccm;

/**
 * Performs Continuus checkout command.
 *
 */
public class CCMCheckout extends CCMCheck {

    /**
     * default constructor
     */
    public CCMCheckout() {
        super();
        setCcmAction(COMMAND_CHECKOUT);
    }
}

