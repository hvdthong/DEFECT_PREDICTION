package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;


/**
 * Exits the active build, giving an additional message
 * if available.
 *
 * @author <a href="mailto:nico@seessle.de">Nico Seessle</a>
 *
 * @since Ant 1.2
 *
 * @ant.task name="fail" category="control"
 */
public class Exit extends Task { 
    private String message;
    private String ifCondition, unlessCondition;

    /**
     * A message giving further information on why the build exited.
     *
     * @param value message to output
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Only fail if a property of the given name exists in the current project.
     * @param c property name
     */
    public void setIf(String c) {
        ifCondition = c;
    }

    /**
     * Only fail if a property of the given name does not
     * exist in the current project.
     * @param c property name
     */
    public void setUnless(String c) {
        unlessCondition = c;
    }

    public void execute() throws BuildException {
        if (testIfCondition() && testUnlessCondition()) {
            if (message != null && message.length() > 0) { 
                throw new BuildException(message);
            } else {
                throw new BuildException("No message");
            }
        }
    }

    /**
     * Set a multiline message.
     */
    public void addText(String msg) {
        if (message == null) {
            message = "";
        }
        message += project.replaceProperties(msg);
    }

    private boolean testIfCondition() {
        if (ifCondition == null || "".equals(ifCondition)) {
            return true;
        }
        
        return project.getProperty(ifCondition) != null;
    }

    private boolean testUnlessCondition() {
        if (unlessCondition == null || "".equals(unlessCondition)) {
            return true;
        }
        return project.getProperty(unlessCondition) == null;
    }

}
