package org.apache.tools.ant.taskdefs.optional.ccm;

import java.util.Date;

/**
 * Performs Continuus checkin command.
 *
 * @author Benoit Moussaud benoit.moussaud@criltelecom.com
 */
public class CCMCheckin extends CCMCheck {

    public CCMCheckin() {
        super();
        setCcmAction(COMMAND_CHECKIN);
        setComment("Checkin " + new Date());
    }

}

