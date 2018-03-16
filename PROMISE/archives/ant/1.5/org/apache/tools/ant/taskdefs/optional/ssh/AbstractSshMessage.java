package org.apache.tools.ant.taskdefs.optional.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.text.NumberFormat;

import org.apache.tools.ant.BuildException;

public abstract class AbstractSshMessage {

    private Session session;
    private LogListener listener = new LogListener() {
        public void log(String message) {
        }
    };

    public AbstractSshMessage(Session session) {
        this.session = session;
    }

    protected Channel openExecChannel(String command) throws JSchException {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);

        return channel;
    }

    protected void sendAck(OutputStream out) throws IOException {
        byte[] buf = new byte[1];
        buf[0] = 0;
        out.write(buf);
        out.flush();
    }

    /**
     * Reads the response, throws a BuildException if the response
     * indicates an error.
     */
    protected void waitForAck(InputStream in) 
        throws IOException, BuildException {
        int b = in.read();


        if (b == -1) {
            throw new BuildException("No response from server");
        } else if (b != 0) {
            StringBuffer sb = new StringBuffer();

            int c = in.read();
            while (c > 0 && c != '\n') {
                sb.append((char) c);
                c = in.read();
            }
            
            if (b == 1) {
                throw new BuildException("server indicated an error: "
                                         + sb.toString());
            } else if (b == 2) {
                throw new BuildException("server indicated a fatal error: "
                                         + sb.toString());
            } else {
                throw new BuildException("unknown response, code " + b
                                         + " message: " + sb.toString());
            }
        }
    }

    public abstract void execute() throws IOException, JSchException;

    public void setLogListener(LogListener aListener) {
        listener = aListener;
    }

    protected void log(String message) {
        listener.log(message);
    }

    protected void logStats(long timeStarted,
                             long timeEnded,
                             int totalLength) {
        double duration = (timeEnded - timeStarted) / 1000.0;
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(1);
        listener.log("File transfer time: " + format.format(duration)
            + " Average Rate: " + format.format(totalLength / duration)
            + " B/s");
    }
}
