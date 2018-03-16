package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

import java.io.IOException;

/**
 * Condition to wait for a TCP/IP socket to have a listener. Its attribute(s) are:
 *   server - the name of the server.
 *   port - the port number of the socket.
 *
 * @author <a href="mailto:denis@network365.com">Denis Hennessy</a>
 * @since Ant 1.5
 */
public class Socket extends ProjectComponent implements Condition {
    private String server = null;
    private int port = 0;

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean eval() throws BuildException {
        if (server == null) {
            throw new BuildException("No server specified in socket "
                                     + "condition");
        }
        if (port == 0) {
            throw new BuildException("No port specified in socket condition");
        }
        log("Checking for listener at " + server + ":" + port, 
            Project.MSG_VERBOSE);
        try {
            java.net.Socket socket = new java.net.Socket(server, port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
