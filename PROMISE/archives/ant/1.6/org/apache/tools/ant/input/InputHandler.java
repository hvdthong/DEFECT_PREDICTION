package org.apache.tools.ant.input;

/**
 * Plugin to Ant to handle requests for user input.
 *
 * @since Ant 1.5
 */
public interface InputHandler {

    /**
     * Handle the request encapsulated in the argument.
     *
     * <p>Precondition: the request.getPrompt will return a non-null
     * value.</p>
     *
     * <p>Postcondition: request.getInput will return a non-null
     * value, request.isInputValid will return true.</p>
     * @param request the request to be processed
     * @throws org.apache.tools.ant.BuildException if the input cannot be read from the console
     */
    void handleInput(InputRequest request)
        throws org.apache.tools.ant.BuildException;
}
