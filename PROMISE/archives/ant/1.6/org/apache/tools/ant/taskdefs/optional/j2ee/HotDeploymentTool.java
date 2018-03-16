package org.apache.tools.ant.taskdefs.optional.j2ee;

import org.apache.tools.ant.BuildException;

/**
 *  An interface for vendor-specific "hot" deployment tools.
 *
 *
 *  @see org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool
 *  @see org.apache.tools.ant.taskdefs.optional.j2ee.ServerDeploy
 */
public interface HotDeploymentTool {
    /** The delete action String **/
    public static final String ACTION_DELETE = "delete";

    /** The deploy action String **/
    public static final String ACTION_DEPLOY = "deploy";

    /** The list action String **/
    public static final String ACTION_LIST = "list";

    /** The undeploy action String **/
    public static final String ACTION_UNDEPLOY = "undeploy";

    /** The update action String **/
    public static final String ACTION_UPDATE = "update";

    /**
     *  Validates the passed in attributes.
     *  @exception org.apache.tools.ant.BuildException if the attributes are invalid or incomplete.
     */
    public void validateAttributes() throws BuildException;

    /**
     *  Perform the actual deployment.
     *  @exception org.apache.tools.ant.BuildException if the attributes are invalid or incomplete.
     */
    public void deploy() throws BuildException;

    /**
     *  Sets the parent task.
     *  @param task A ServerDeploy object representing the parent task.
     */
    public void setTask(ServerDeploy task);
}
