package org.apache.synapse;

import org.apache.axis2.util.OptionsParser;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.ListenerManager;
import org.apache.axis2.description.TransportInDescription;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.Iterator;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts all transports as specified on the axis2.xml
 */
public class SynapseServer {

    private static final Log log = LogFactory.getLog(SynapseServer.class);

    public static void printUsage() {
        System.out.println("Usage: SynapseServer <repository>");
        System.out.println(" Opts: -? this message");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 1 || !new File(args[0]).exists()) {
            printUsage();
        }

        System.out.println("[SynapseServer] Using the Axis2 Repository "
                + new File(args[0]).getAbsolutePath());

        try {
            ConfigurationContext configctx = ConfigurationContextFactory.
                createConfigurationContextFromFileSystem(args[0], null);

            ListenerManager listenerManager = configctx.getListenerManager();
            if (listenerManager == null) {
                listenerManager = new ListenerManager();
                listenerManager.init(configctx);
            }

            selectPort(configctx);

            Iterator iter = configctx.getAxisConfiguration().
                getTransportsIn().keySet().iterator();
            while (iter.hasNext()) {
                String trp = (String) iter.next();
                TransportInDescription trsIn = (TransportInDescription)
                    configctx.getAxisConfiguration().getTransportsIn().get(trp);
                listenerManager.addListener(trsIn, false);
                String msg = "[SynapseServer] Starting transport " + trsIn.getName();
                if (trsIn.getParameter("port") != null) {
                    msg += " on port " + trsIn.getParameter("port").getValue();
                }
                System.out.println(msg);
            }
            System.out.println("[SynapseServer] Ready");

        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("[SynapseServer] Startup failed...");
        }
    }

    private static void selectPort(ConfigurationContext configCtx) {
        TransportInDescription trsIn = (TransportInDescription)
            configCtx.getAxisConfiguration().getTransportsIn().get("http");

        if (trsIn != null) {

            int port = 8080;

            String strPort = System.getProperty("port");
            if(strPort != null) {
                try {
                    port = new Integer(strPort).intValue();
                } catch (NumberFormatException e) {
                    log.error("Given port is not a valid integer. Port specified in the configuration is used for the server.");
                    port = Integer.parseInt(trsIn.getParameter("port").getValue().toString());
                }

            } else {
                port = Integer.parseInt(trsIn.getParameter("port").getValue().toString());
            }

            while (true) {
                ServerSocket sock = null;
                try {
                    sock = new ServerSocket(port);
                    trsIn.getParameter("port").setValue(Integer.toString(port));
                    break;
                } catch (Exception e) {
                	System.out.println("[SynapseServer] Port "+port+" already in use. Trying alternate");
                    if (port == 8080) {
                        port = 8008;
                    } else {
                        port++;
                    }
                } finally {
                    if (sock != null) {
                        try {
                            sock.close();
                        } catch (Exception e) {}
                    }
                }
            }
        }
    }

}
