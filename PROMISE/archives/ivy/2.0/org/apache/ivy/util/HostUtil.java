package org.apache.ivy.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains basic helper methods for the Host. 
 * 
 */
public final class HostUtil {
    
    private static String localHostName = null;
    
    /**
     * This default constructor is to hide this class from initialization through other classes.
     */
    private HostUtil() {
    }
    
    /**
     * This method returns the name of the current Host, if this name cannot be determined,
     * localhost will be returned. 
     * 
     * @return The name of the current "local" Host.
     */
    public static String getLocalHostName() {
        if (localHostName == null) {
            try {
                localHostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                localHostName = "localhost";
            }
        }
        return localHostName;
    }
}
