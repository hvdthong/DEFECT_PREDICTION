package org.apache.tools.ant.taskdefs.optional.ccm;

import java.util.Date;

/**
 * Performs Continuus checkin command.
 *
 */
public class CCMCheckin extends CCMCheck {

    /**
     * Default constructor - setup checkin command
     */
    public CCMCheckin() {
        super();
        setCcmAction(COMMAND_CHECKIN);
        setComment("Checkin " + new Date());
    }

}

