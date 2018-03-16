package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/** 
 * Obtains or sets the value of a counter.
 *
 * <p> When used in its base form
 * (where only the counter name is provided), the counter value will be
 * printed to the output stream. When the value is provided, the counter
 * will be set to the value provided. When a property name is provided,
 * the property will be filled with the value of the counter. You may
 * not specify to both get and set the value of the counter in the same
 * Task.
 * </p>
 * <P>
 * The user performing this task must have Perforce &quot;review&quot; permissions
 * as defined by Perforce protections in order for this task to succeed.
</P>
 
 * Example Usage:<br>
 * &lt;p4counter name="${p4.counter}" property=${p4.change}"/&gt;
 * @author <a href="mailto:kirk@radik.com">Kirk Wylie</a>
 */

public class P4Counter extends P4Base {
    public String counter = null;
    public String property = null;
    public boolean shouldSetValue = false;
    public boolean shouldSetProperty = false;
    public int value = 0;

    /**
     * The name of the counter; required
     */
    public void setName(String counter) {
        this.counter = counter;
    }

    /**
     * The new value for the counter; optional.
     */
    public void setValue(int value) {
        this.value = value;
        shouldSetValue = true;
    }

    /**
     * A property to be set with the value of the counter
     */
    public void setProperty(String property) {
        this.property = property;
        shouldSetProperty = true;
    }

    /**
     * again, properties are mutable in this tsk
     */
    public void execute() throws BuildException {

        if ((counter == null) || counter.length() == 0) {
            throw new BuildException("No counter specified to retrieve");
        }

        if (shouldSetValue && shouldSetProperty) {
            throw new BuildException("Cannot both set the value of the property and retrieve the value of the property.");
        }

        String command = "counter " + P4CmdOpts + " " + counter;
        if (!shouldSetProperty) {
            command = "-s " + command;
        }
        if (shouldSetValue) {
            command += " " + value;
        }

        if (shouldSetProperty) {
            final Project myProj = project;

            P4Handler handler = new P4HandlerAdapter() {
                public void process(String line) {
                    log("P4Counter retrieved line \"" + line + "\"", Project.MSG_VERBOSE);
                    try {
                        value = Integer.parseInt(line);
                        myProj.setProperty(property, "" + value);
                    } catch (NumberFormatException nfe) {
                        throw new BuildException("Perforce error. Could not retrieve counter value.");
                    }
                }
            };

            execP4Command(command, handler);
        } else {
            execP4Command(command, new SimpleP4OutputHandler(this));
        }
    }
}
