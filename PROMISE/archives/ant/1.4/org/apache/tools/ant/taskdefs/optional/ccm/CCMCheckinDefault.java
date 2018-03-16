package org.apache.tools.ant.taskdefs.optional.ccm;

/**
 * Task to perform Checkin Default task command to Continuus
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMCheckinDefault extends CCMCheck {
    
    public CCMCheckinDefault(){
        super();
        setCcmAction(COMMAND_CHECKIN);
        setTask(DEFAULT_TASK);
    }

    public static final String DEFAULT_TASK = "default";   
}

