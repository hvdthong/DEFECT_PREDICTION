package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

/**
 * Condition to wait for a TCP/IP socket to have a listener. Its attributes are:
 *   server - the name of the server.
 *   port - the port number of the socket.
 *
 * @since Ant 1.5
 */
public class Socket extends ProjectComponent implements Condition {
    private String server = null;
    private int port = 0;

    /**
     * Set the server attribute
     *
     * @param server the server name
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Set the port attribute
     *
     * @param port the port number of the socket
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return true if a socket can be created
     * @exception BuildException if the attributes are not set
     */
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
        java.net.Socket s = null;
        try {
            s = new java.net.Socket(server, port);
        } catch (IOException e) {
            return false;
        } finally {
          if (s != null) {
            try {
              s.close();
            } catch (IOException ioe) {
            }
          }
        }
        return true;
    }

}
