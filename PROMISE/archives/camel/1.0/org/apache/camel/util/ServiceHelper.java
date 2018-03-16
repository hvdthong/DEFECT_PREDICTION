package org.apache.camel.util;

import org.apache.camel.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * A collection of helper methods for working with {@link Service} objects
 *
 * @version $Revision: 550575 $
 */
public class ServiceHelper {
    private static final transient Log log = LogFactory.getLog(ServiceHelper.class);


    /**
     * Starts all of the given services
     */
    public static void startServices(Object... services) throws Exception {
        for (Object value : services) {
            startService(value);
        }
    }

    /**
     * Starts all of the given services
     */
    public static void startServices(Collection services) throws Exception {
        for (Object value : services) {
            if (value instanceof Service) {
                Service service = (Service) value;
                service.start();
            }
        }
    }

    public static void startService(Object value) throws Exception {
        if (value instanceof Service) {
            Service service = (Service) value;
            service.start();
        }
        else if (value instanceof Collection) {
            startServices((Collection) value);
        }
    }

    /**
     * Stops all of the given services, throwing the first exception caught
     */
    public static void stopServices(Object... services) throws Exception {
        Exception firstException = null;
        for (Object value : services) {
            if (value instanceof Service) {
                Service service = (Service) value;
                try {
                    service.stop();
                }
                catch (Exception e) {
                    log.debug("Caught exception shutting down: " + e, e);
                    if (firstException == null) {
                        firstException = e;
                    }
                }
            }
        }
        if (firstException != null) {
            throw firstException;
        }
    }
    /**
     * Stops all of the given services, throwing the first exception caught
     */
    public static void stopServices(Collection services) throws Exception {
        Exception firstException = null;
        for (Object value : services) {
            if (value instanceof Service) {
                Service service = (Service) value;
                try {
                    service.stop();
                }
                catch (Exception e) {
                    log.debug("Caught exception shutting down: " + e, e);
                    if (firstException == null) {
                        firstException = e;
                    }
                }
            }
        }
        if (firstException != null) {
            throw firstException;
        }
    }
}
