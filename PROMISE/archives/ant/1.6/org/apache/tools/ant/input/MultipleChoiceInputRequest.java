package org.apache.tools.ant.input;

import java.util.Vector;

/**
 * Encapsulates an input request.
 *
 * @since Ant 1.5
 */
public class MultipleChoiceInputRequest extends InputRequest {
    private Vector choices = new Vector();

    /**
     * @param prompt The prompt to show to the user.  Must not be null.
     * @param choices holds all input values that are allowed.
     *                Must not be null.
     */
    public MultipleChoiceInputRequest(String prompt, Vector choices) {
        super(prompt);
        if (choices == null) {
            throw new IllegalArgumentException("choices must not be null");
        }
        this.choices = choices;
    }

    /**
     * @return The possible values.
     */
    public Vector getChoices() {
        return choices;
    }

    /**
     * @return true if the input is one of the allowed values.
     */
    public boolean isInputValid() {
        return choices.contains(getInput());
    }
}
