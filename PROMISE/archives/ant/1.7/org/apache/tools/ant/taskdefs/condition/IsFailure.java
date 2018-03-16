package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.taskdefs.Execute;

/**
 * Condition to test a return-code for failure.
 * @since Ant 1.7
 */
public class IsFailure implements Condition {
    private int code;

    /**
     * Set the return code to check.
     * @param c the return code.
     */
    public void setCode(int c) {
        code = c;
    }

    /**
     * Get the return code that will be checked by this IsFailure condition.
     * @return return code as int.
     */
    public int getCode() {
        return code;
    }

    /**
     * Fulfill the condition interface.
     * @return the result of evaluating the specified return code.
     */
    public boolean eval() {
        return Execute.isFailure(code);
    }

}
