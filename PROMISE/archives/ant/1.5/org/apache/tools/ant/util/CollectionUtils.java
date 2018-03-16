package org.apache.tools.ant.util;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A set of helper methods related to collection manipulation.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @since Ant 1.5
 */
public class CollectionUtils {

    /**
     * Vector.equals() doesn't do any good in 1.1
     *
     * @since Ant 1.5
     */
    public static boolean equals(Vector v1, Vector v2) {
        if (v1 == v2) {
            return true;
        }
        
        if (v1 == null || v2 == null) {
            return false;
        }

        if (v1.size() != v2.size()) {
            return false;
        }

        Enumeration e1 = v1.elements();
        Enumeration e2 = v2.elements();
        while (e1.hasMoreElements()) {
            if (!e1.nextElement().equals(e2.nextElement())) {
                return false;
            }
        }
        

        return true;
    }

    /**
     * Hashtable.equals() doesn't do any good in 1.1
     *
     * <p>Follows the equals contract of Java 2's Map.</p>
     *
     * @since Ant 1.5
     */
    public static boolean equals(Dictionary d1, Dictionary d2) {
        if (d1 == d2) {
            return true;
        }
        
        if (d1 == null || d2 == null) {
            return false;
        }

        if (d1.size() != d2.size()) {
            return false;
        }

        Enumeration e1 = d1.keys();
        while (e1.hasMoreElements()) {
            Object key = e1.nextElement();
            Object value1 = d1.get(key);
            Object value2 = d2.get(key);
            if (value2 == null || !value1.equals(value2)) {
                return false;
            }
        }
        

        return true;
    }
}
