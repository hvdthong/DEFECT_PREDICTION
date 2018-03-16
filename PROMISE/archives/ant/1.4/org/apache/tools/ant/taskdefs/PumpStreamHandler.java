package org.apache.tools.ant.taskdefs;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Copies standard output and error of subprocesses to standard output and
 * error of the parent process.
 *
 * TODO: standard input of the subprocess is not implemented.
 *
 * @author thomas.haas@softwired-inc.com
 */
public class PumpStreamHandler implements ExecuteStreamHandler {

    private Thread inputThread;
    private Thread errorThread;

    private OutputStream out, err;

    public PumpStreamHandler(OutputStream out, OutputStream err) {
        this.out = out;
        this.err = err;
    }

    public PumpStreamHandler(OutputStream outAndErr) {
        this(outAndErr, outAndErr);
    }

    public PumpStreamHandler() {
        this(System.out, System.err);
    }

    public void setProcessOutputStream(InputStream is) {
        createProcessOutputPump(is, out);
    }


    public void setProcessErrorStream(InputStream is) {
        createProcessErrorPump(is, err);
    }


    public void setProcessInputStream(OutputStream os) {
    }


    public void start() {
        inputThread.start();
        errorThread.start();
    }


    public void stop() {
        try {
            inputThread.join();
        } catch(InterruptedException e) {}
        try {
            errorThread.join();
        } catch(InterruptedException e) {}
        try {
            err.flush();
        } catch (IOException e) {}
        try {
            out.flush();
        } catch (IOException e) {}
    }

    protected OutputStream getErr() {
        return err;
    }

    protected OutputStream getOut() {
        return out;
    }

    protected void createProcessOutputPump(InputStream is, OutputStream os) {
        inputThread = createPump(is, os);
    }

    protected void createProcessErrorPump(InputStream is, OutputStream os) {
        errorThread = createPump(is, os);
    }


    /**
     * Creates a stream pumper to copy the given input stream to the given output stream.
     */
    protected Thread createPump(InputStream is, OutputStream os) {
        final Thread result = new Thread(new StreamPumper(is, os));
        result.setDaemon(true);
        return result;
    }

}
