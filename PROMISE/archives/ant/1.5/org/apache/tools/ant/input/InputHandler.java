package org.apache.tools.ant.input;

/**
 * Plugin to Ant to handle requests for user input.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 274041 $
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
     */
    void handleInput(InputRequest request) 
        throws org.apache.tools.ant.BuildException;
}
