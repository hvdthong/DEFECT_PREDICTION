package org.apache.tools.ant.taskdefs.optional.ccm;

/**
 * Performs Continuus checkout command.
 *
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMCheckout extends CCMCheck {

    public CCMCheckout() {
        super();
        setCcmAction(COMMAND_CHECKOUT);
    }
}

