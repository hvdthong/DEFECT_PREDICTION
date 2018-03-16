package org.apache.synapse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

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

        ServerManager serverManager = ServerManager.getInstance();
        serverManager.setAxis2Repolocation(args[0]);
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
