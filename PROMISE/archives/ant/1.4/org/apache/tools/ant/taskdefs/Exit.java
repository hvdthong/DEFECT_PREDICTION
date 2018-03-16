package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

/**
 * Just exit the active build, giving an additional message 
 * if available.
 *
 * @author Nico Seessle <nico@seessle.de>
 */
public class Exit extends Task { 
    private String message;
    
    public void setMessage(String value) { 
        this.message = value;
    }
    
    public void execute() throws BuildException {
        if (message != null && message.length() > 0) { 
            throw new BuildException(message);
        } else {
            throw new BuildException("No message");
        }
    }

    /**
     * Set a multiline message.
     */
    public void addText(String msg) {
        message += 
            ProjectHelper.replaceProperties(project, msg, project.getProperties());
    }

}
