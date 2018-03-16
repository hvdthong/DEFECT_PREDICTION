package org.apache.ivy.util;

/**
 * Memory related utilities.
 */
public final class MemoryUtil {
    private static final int SAMPLING_SIZE = 100;
    private static final int SLEEP_TIME = 100;
    
    private MemoryUtil() {
    }

    /**
     * Returns the approximate size of a default instance of the given class.
     * 
     * @param clazz
     *            the class to evaluate.
     * @return the estimated size of instance, in bytes.
     */
    public static long sizeOf(Class clazz) {
        long size = 0;
        Object[] objects = new Object[SAMPLING_SIZE];
        try {
            clazz.newInstance();
            long startingMemoryUse = getUsedMemory();
            for (int i = 0; i < objects.length; i++) {
                objects[i] = clazz.newInstance();
            }
            long endingMemoryUse = getUsedMemory();
            float approxSize = (endingMemoryUse - startingMemoryUse) / (float) objects.length;
            size = Math.round(approxSize);
        } catch (Exception e) {
            System.out.println("WARNING:couldn't instantiate" + clazz);
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Returns the currently used memory, after calling garbage collector and waiting enough to get
     * maximal chance it is actually called. But since {@link Runtime#gc()} is only advisory,
     * results returned by this method should be treated as rough approximation only.
     * 
     * @return the currently used memory, in bytes.
     */
    public static long getUsedMemory() {
        gc();
        long totalMemory = Runtime.getRuntime().totalMemory();
        gc();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        return usedMemory;
    }

    private static void gc() {
        try {
            System.gc();
            Thread.sleep(SLEEP_TIME);
            System.runFinalization();
            Thread.sleep(SLEEP_TIME);
            System.gc();
            Thread.sleep(SLEEP_TIME);
            System.runFinalization();
            Thread.sleep(SLEEP_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(sizeOf(Class.forName(args[0])));
    }
}
