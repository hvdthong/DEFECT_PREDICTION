package org.apache.tools.ant.taskdefs.optional.ccm;

/**
 * Task to perform Checkout command to Continuus
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMCheckout extends CCMCheck {
    
    public CCMCheckout(){
        super();
        setCcmAction(COMMAND_CHECKOUT);
    }
}

