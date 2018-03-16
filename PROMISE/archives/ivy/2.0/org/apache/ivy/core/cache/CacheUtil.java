package org.apache.ivy.core.cache;

/**
 * Utility class providing some cache related facilities. 
 *
 */
public final class CacheUtil {

    /**
     * Checks that the given pattern is acceptable as a cache pattern
     * 
     * @param cachePattern
     *            the pattern to check
     * @throws IllegalArgumentException
     *             if the pattern isn't acceptable as cache pattern
     */
    public static void checkCachePattern(String cachePattern) {
        if (cachePattern == null) {
            throw new IllegalArgumentException("null cache pattern not allowed.");
        }
        if (cachePattern.startsWith("..")) {
            throw new IllegalArgumentException("invalid cache pattern: '" + cachePattern 
                + "': cache patterns must not lead outside cache directory");
        }
        if (cachePattern.startsWith("/")) {
            throw new IllegalArgumentException("invalid cache pattern: '" + cachePattern 
                + "': cache patterns must not be absolute");
        }
    }

    private CacheUtil() {
    }
}
