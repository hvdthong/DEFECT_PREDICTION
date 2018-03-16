package org.apache.tools.ant.taskdefs.optional.ccm;

/**
 * Performs Continuus Checkin Default task command.
 *
 *
 * @ant.task name="ccmcheckintask" category="scm"
 */
public class CCMCheckinDefault extends CCMCheck {

    public CCMCheckinDefault() {
        super();
        setCcmAction(COMMAND_CHECKIN);
        setTask(DEFAULT_TASK);
    }

    public static final String DEFAULT_TASK = "default";
}

