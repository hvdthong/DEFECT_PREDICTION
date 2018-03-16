package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * Condition that tests whether a given string evals to true
 *
 * @since Ant 1.5
 */
public class IsTrue extends ProjectComponent implements Condition {
    /**
     * what we eval
     */
    private Boolean value = null;

    /**
     * set the value to be tested; let ant eval it to true/false
     * @param value the value to test
     */
    public void setValue(boolean value) {
        this.value = value ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * @return the value
     * @throws BuildException if someone forgot to spec a value
     */
    public boolean eval() throws BuildException {
        if (value == null) {
            throw new BuildException("Nothing to test for truth");
        }
        return value.booleanValue();
    }

}

