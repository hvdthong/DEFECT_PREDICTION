package org.apache.camel.util;

import org.apache.camel.converter.ObjectConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A helper class for various {@link System} related methods
 *
 * @version $Revision: 673954 $
 */
public final class SystemHelper {
    private static final transient Log LOG = LogFactory.getLog(SystemHelper.class);

    private SystemHelper() {
    }

    /**
     * Looks up the given system property name returning null if any exceptions occur
     */
    public static String getSystemProperty(String name) {
        try {
            return System.getProperty(name);
        } catch (Exception e) {
            LOG.debug("Caught exception looking for system property: " + name + " exception: " + e, e);
            return null;
        }
    }

    /**
     * Looks up the given system property boolean value. Returns false if the system property doesn't exist.
     */
    public static boolean isSystemProperty(String name) {
        String text = getSystemProperty(name);
        return ObjectConverter.toBool(text);
    }

}
