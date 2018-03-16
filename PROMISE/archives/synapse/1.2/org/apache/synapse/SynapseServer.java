package org.apache.synapse;

import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * This is the class invoked by the command line scripts synapse.sh and synapse-daemon.sh to
 * start an instance of Synapse. This class calls on the ServerManager to start up the instance
 *
 * TODO Switch to using commons-cli and move all command line parameter processing etc from the
 * .sh and .bat into this.. for 1.3 release :)
 */
public class SynapseServer {

    private static final Log log = LogFactory.getLog(SynapseServer.class);

    private static final String USAGE_TXT =
        "Usage: SynapseServer <axis2_repository> <axis2_xml> <synapse_home> <synapse_xml> <resolve_root>" +
        "\n Opts: -? this message";

    public static void printUsage() {
        System.out.println(USAGE_TXT);
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 1 && args.length != 4 && args.length != 5 && args.length != 6) {
            printUsage();
        }

        ServerManager serverManager = ServerManager.getInstance();
        serverManager.setAxis2Repolocation(args[0]);
        if (args.length == 1) {
            log.warn("Configuring server manager using deprecated system properties; please update your configuration");
            serverManager.setAxis2Xml(System.getProperty(Constants.AXIS2_CONF));
            serverManager.setSynapseHome(System.getProperty(SynapseConstants.SYNAPSE_HOME));
            serverManager.setSynapseXMLPath(System.getProperty(SynapseConstants.SYNAPSE_XML));
            serverManager.setResolveRoot(System.getProperty(SynapseConstants.RESOLVE_ROOT));
        } else if(args.length == 4) {
            serverManager.setAxis2Xml(args[1]);
            serverManager.setSynapseHome(args[2]);
            serverManager.setSynapseXMLPath(args[3]);
            serverManager.setResolveRoot(args[2] + File.separator + "repository");
        } else if(args.length == 5) {
            serverManager.setAxis2Xml(args[1]);
            serverManager.setSynapseHome(args[2]);
            serverManager.setSynapseXMLPath(args[3]);
            serverManager.setResolveRoot(args[4]);
        } else if(args.length == 6) {
            serverManager.setAxis2Xml(args[1]);
            serverManager.setSynapseHome(args[2]);
            serverManager.setSynapseXMLPath(args[3]);
            serverManager.setResolveRoot(args[4]);
            serverManager.setServerName(args[5]);
        }
        
        serverManager.start();
        addShutdownHook();
    }

    private static void addShutdownHook() {
        Thread shutdownHook = new Thread() {
            public void run() {
                log.info("Shutting down Apache Synapse ...");
                try {
                    ServerManager.getInstance().stop();
                    log.info("Shutdown complete");
                    log.info("Halting JVM");
                } catch (Exception e) {
                    log.warn("Error occurred while shutting down Apache Synapse : " + e);
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
