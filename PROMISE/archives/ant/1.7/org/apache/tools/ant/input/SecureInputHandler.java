package org.apache.tools.ant.input;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.ReflectUtil;

/**
 * Prompts and requests input.  May loop until a valid input has
 * been entered. Doesn't echo input (requires Java6). If Java6 is not
 * available, fallsback to the DefaultHandler (insecure).
 * @since Ant 1.7.1
 */
public class SecureInputHandler extends DefaultInputHandler {

    /**
     * Default no-args constructor
     */
    public SecureInputHandler() {
    }

    /**
     * Handle the input
     * @param request the request to handle
     * @throws BuildException if not possible to read from console
     */
    public void handleInput(InputRequest request) throws BuildException {
        String prompt = getPrompt(request);
        try {
            Class system = Class.forName("java.lang.System");
            Object console = ReflectUtil.invokeStatic(system, "console");
            do {
                char[] input = (char[]) ReflectUtil.invoke(
                    console, "readPassword", String.class, prompt,
                    Object[].class, (Object[]) null);
                request.setInput(new String(input));
                /* for security zero char array after retrieving value */
                java.util.Arrays.fill(input, ' ');
            } while (!request.isInputValid());
        } catch (Exception e) {
            /* Java6 not present use default handler */
            super.handleInput(request);
        }
    }
}
