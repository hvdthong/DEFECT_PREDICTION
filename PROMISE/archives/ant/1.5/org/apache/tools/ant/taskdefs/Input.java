package org.apache.tools.ant.taskdefs;

import  java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputRequest;
import org.apache.tools.ant.input.MultipleChoiceInputRequest;
import org.apache.tools.ant.util.StringUtils;

/**
 * Reads an input line from the console.
 *
 * @author <a href="mailto:usch@usch.net">Ulrich Schmidt</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @since Ant 1.5
 *
 * @ant.task category="control"
 */
public class Input extends Task {
    private String validargs = null;
    private String message = "";
    private String addproperty = null;

    /**
     * Defines valid input parameters as comma separated strings. If set, input
     * task will reject any input not defined as accepted and requires the user
     * to reenter it. Validargs are case sensitive. If you want 'a' and 'A' to
     * be accepted you need to define both values as accepted arguments.
     *
     * @param validargs A comma separated String defining valid input args.
     */
    public void setValidargs (String validargs) {
        this.validargs = validargs;
    }

    /**
     * Defines the name of a property to be created from input. Behaviour is
     * according to property task which means that existing properties
     * cannot be overriden.
     *
     * @param addproperty Name for the property to be created from input
     */
    public void setAddproperty (String addproperty) {
        this.addproperty = addproperty;
    }

    /**
     * Sets the Message which gets displayed to the user during the build run.
     * @param message The message to be displayed.
     */
    public void setMessage (String message) {
        this.message = message;
    }

    /**
     * Set a multiline message.
     */
    public void addText(String msg) {
        message += getProject().replaceProperties(msg);
    }

    /**
     * No arg constructor.
     */
    public Input () {
    }

    /**
     * Actual method executed by jakarta-ant.
     * @exception BuildException
     */
    public void execute () throws BuildException {
        InputRequest request = null;
        if (validargs != null) {
            Vector accept = StringUtils.split(validargs, ',');
            request = new MultipleChoiceInputRequest(message, accept);
        } else {
            request = new InputRequest(message);
        }

        getProject().getInputHandler().handleInput(request);

        String value = request.getInput();
        if (addproperty != null && value != null) {
            project.setNewProperty(addproperty, value);
        }
    }

}
