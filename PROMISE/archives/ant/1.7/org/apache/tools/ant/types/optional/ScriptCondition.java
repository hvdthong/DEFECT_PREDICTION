package org.apache.tools.ant.types.optional;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * A condition that lets you include script.
 * The condition component sets a bean "self", whose attribute "value"
 * must be set to true for the condition to succeed, false to fail.
 * The default is 'false'
 */
public class ScriptCondition extends AbstractScriptComponent implements Condition {

    /**
     * result field
     */
    private boolean value = false;

    /**
     * Is this condition true?
     *
     * @return true if the condition is true
     *
     * @throws org.apache.tools.ant.BuildException
     *          if an error occurs
     */
    public boolean eval() throws BuildException {
        initScriptRunner();
        evaluateScript("ant_condition");
        return getValue();
    }

    /**
     * get the current value of the conditon
     * @return true if the condition
     */
    public boolean getValue() {
        return value;
    }

    /**
     * set the value of the condition.
     * This is used by the script to pass the return value.
     * It can be used by an attribute, in which case it sets the default
     * value
     * @param value the value to set the condition to
     */
    public void setValue(boolean value) {
        this.value = value;
    }
}
