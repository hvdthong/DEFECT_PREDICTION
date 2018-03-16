package org.apache.camel.management;

/**
 * Utility class providing RFC 1738 style encoding for ObjectName values.
 * (see section 2.2).
 *
 * Key Property Values in ObjectName(s) may not contain one of :",=*?
 * (see jmx_1.2_spec, Chapter 6)
 *
 * @author hzbarcea
 *
 */
public final class ObjectNameEncoder {

    private ObjectNameEncoder() {
    }

    public static String encode(String on) {
        return encode(on, false);
    }

    public static String encode(String on, boolean ignoreWildcards) {
        on = on.replace(":", "%3a");
        on = on.replace("\"", "%22");
        on = on.replace(",", "%2c");
        on = on.replace("=", "%3d");
        if (!ignoreWildcards) {
            on = on.replace("*", "%2a");
            on = on.replace("?", "%3f");
        }
        return on;
    }

    public static String decode(String on) {
        on = on.replace("%25", "%");
        on = on.replace("%3a", ":");
        on = on.replace("%22", "\"");
        on = on.replace("%2c", ",");
        on = on.replace("%3d", "=");
        on = on.replace("%2a", "*");
        on = on.replace("%3f", "?");
        return on;
    }
}
