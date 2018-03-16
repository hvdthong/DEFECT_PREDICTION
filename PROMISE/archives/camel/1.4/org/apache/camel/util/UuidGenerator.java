package org.apache.camel.util;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generator for Globally unique Strings.
 */
public class UuidGenerator {

    private static final transient Log LOG = LogFactory.getLog(UuidGenerator.class); 
    private static final String UNIQUE_STUB;
    private static int instanceCount;
    private static String hostName;
    private String seed;
    private long sequence;

    static {
        String stub = "";
        boolean canAccessSystemProps = true;
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPropertiesAccess();
            }
        } catch (SecurityException se) {
            canAccessSystemProps = false;
        }

        if (canAccessSystemProps) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
                ServerSocket ss = new ServerSocket(0);
                stub = "/" + ss.getLocalPort() + "-" + System.currentTimeMillis() + "/";
                Thread.sleep(100);
                ss.close();
            } catch (Exception ioe) {
                LOG.warn("Could not generate unique stub", ioe);
            }
        } else {
            hostName = "localhost";
            stub = "-1-" + System.currentTimeMillis() + "-";
        }
        UNIQUE_STUB = stub;
    }

    public UuidGenerator(String prefix) {
        synchronized (UNIQUE_STUB) {
            this.seed = prefix + UNIQUE_STUB + (instanceCount++) + "-";
        }
    }

    public UuidGenerator() {
        this("ID-" + hostName);
    }

    /**
     * As we have to find the hostname as a side-affect of generating a unique
     * stub, we allow it's easy retrevial here
     * 
     * @return the local host name
     */
    public static String getHostName() {
        return hostName;
    }

    /**
     * Generate a unqiue id
     */
    public synchronized String generateId() {
        return this.seed + (this.sequence++);
    }

    /**
     * Generate a unique ID - that is friendly for a URL or file system
     * 
     * @return a unique id
     */
    public String generateSanitizedId() {
        return generateSanitizedId(generateId());
    }

    /**
     * Ensures that the id is friendly for a URL or file system
     *
     * @param id the unique id
     * @return the id as file friendly id
     */
    public static String generateSanitizedId(String id) {
        id = id.replace(':', '-');
        id = id.replace('_', '-');
        id = id.replace('.', '-');
        return id;
    }

}
