package org.apache.tools.ant.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import org.apache.tools.ant.BuildException;

/**
 * Prompts on System.err, reads input from System.in
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 274041 $
 * @since Ant 1.5
 */
public class DefaultInputHandler implements InputHandler {
    
    /**
     * Empty no-arg constructor
     */
    public DefaultInputHandler() {
    }

    /**
     * Prompts and requests input.  May loop until a valid input has
     * been entered.
     */
    public void handleInput(InputRequest request) throws BuildException {
        String prompt = getPrompt(request);
        BufferedReader in = 
            new BufferedReader(new InputStreamReader(getInputStream()));
        do {
            System.err.println(prompt);
            try {
                String input = in.readLine();
                request.setInput(input);
            } catch (IOException e) {
                throw new BuildException("Failed to read input from Console.",
                                         e);
            }
        } while (!request.isInputValid());
    }

    /**
     * Constructs user prompt from a request.
     *
     * <p>This implementation adds (choice1,choice2,choice3,...) to the
     * prompt for <code>MultipleChoiceInputRequest</code>s.</p>
     *
     * @param request the request to construct the prompt for.
     *                Must not be <code>null</code>.
     */
    protected String getPrompt(InputRequest request) {
        String prompt = request.getPrompt();
        if (request instanceof MultipleChoiceInputRequest) {
            StringBuffer sb = new StringBuffer(prompt);
            sb.append("(");
            Enumeration enum = 
                ((MultipleChoiceInputRequest) request).getChoices().elements();
            boolean first = true;
            while (enum.hasMoreElements()) {
                if (!first) {
                    sb.append(",");
                }
                sb.append(enum.nextElement());
                first = false;
            }
            sb.append(")");
            prompt = sb.toString();
        }
        return prompt;
    }

    /**
     * Returns the input stream from which the user input should be read.
     */
    protected InputStream getInputStream() {
        return System.in;
    }

}
