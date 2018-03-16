package org.apache.tools.ant.input;

/**
 * Encapsulates an input request.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 274041 $
 * @since Ant 1.5
 */
public class InputRequest {
    private String prompt;
    private String input;

    /**
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
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Sets the user provided input.
     */
    public void setInput(String input) {
        this.input = input;
    }
    
    /**
     * Is the user input valid?
     */
    public boolean isInputValid() {
        return true;
    }

    /**
     * Retrieves the user input.
     */
    public String getInput() {
        return input;
    }

}
