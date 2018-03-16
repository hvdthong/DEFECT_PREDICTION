package org.apache.tools.ant.input;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.StreamPumper;
import org.apache.tools.ant.util.FileUtils;

/**
 * Prompts on System.err, reads input from System.in until EOF
 *
 * @since Ant 1.7
 */
public class GreedyInputHandler extends DefaultInputHandler {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Empty no-arg constructor
     */
    public GreedyInputHandler() {
    }

    /**
     * Prompts and requests input.
     * @param request the request to handle
     * @throws BuildException if not possible to read from console,
     *         or if input is invalid.
     */
    public void handleInput(InputRequest request) throws BuildException {
        String prompt = getPrompt(request);
        InputStream in = null;
        try {
            in = getInputStream();
            System.err.println(prompt);
            System.err.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamPumper p = new StreamPumper(in, baos);
            Thread t = new Thread(p);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                try {
                    t.join();
                } catch (InterruptedException e2) {
                }
            }
            request.setInput(new String(baos.toByteArray()));
            if (!(request.isInputValid())) {
                throw new BuildException(
                    "Received invalid console input");
            }
            if (p.getException() != null) {
                throw new BuildException(
                    "Failed to read input from console", p.getException());
            }
        } finally {
            FileUtils.close(in);
        }
    }
}
