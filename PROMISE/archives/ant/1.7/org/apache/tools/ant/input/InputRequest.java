package org.apache.tools.ant.input;

/**
 * Encapsulates an input request.
 *
 * @since Ant 1.5
 */
public class InputRequest {
    private String prompt;
    private String input;
    private String defaultValue;

    /**
     * Construct an InputRequest.
     * @param prompt The prompt to show to the user.  Must not be null.
     */
    public InputRequest(String prompt) {
        if (prompt == null) {
            throw new IllegalArgumentException("prompt must not be null");
        }

        this.prompt = prompt;
    }

    /**
     * Retrieves the prompt text.
     * @return the prompt.
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Sets the user provided input.
     * @param input the string to be used for input.
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Is the user input valid?
     * @return true if it is.
     */
    public boolean isInputValid() {
        return true;
    }

    /**
     * Retrieves the user input.
     * @return the user input.
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets a configured default value.
     * @return the default value.
     * @since Ant 1.7.0
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Configures a default value.
     * @param d the value to set.
     * @since Ant 1.7.0
     */
    public void setDefaultValue(String d) {
        defaultValue = d;
    }

}
