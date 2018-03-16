package org.apache.xerces.utils;

/**
 * Algorithm used to hash char arrays (strings).
 *
 * This class was created after it was discovered that parsing some
 * documents was unexpectedly slow due to many different strings
 * hashing to the same 32-bit value using the java.lang.String hash
 * algorithm.
 *
 * The trick seems to be the shift of the top eight bits of the hashcode
 * back down to the bottom to keep them from being rolled out.
 *
 * @version
 */
public final class StringHasher {
    /**
     * generate a hashcode for a String
     *
     * @param str the String to hash
     * @param strLength the length of the String to hash
     * @return hashcode for the String
     */
    public static int hashString(String str, int strLength) {
        int hashcode = 0;
        for (int i = 0; i < strLength; i++) {
            int top = hashcode >> 24;
            hashcode += ((hashcode * 37) + top + ((int)str.charAt(i)));
        }
        hashcode = (hashcode & 0x7fffffff);
        return (hashcode == 0) ? 1 : hashcode;
    }
    /**
     * generate a hashcode for a character array
     *
     * @param chars the array to hash
     * @param offset the offset to start hashing
     * @param length the length of characters to hash
     * @return hashcode for the character array
     */
    public static int hashChars(char[] chars, int offset, int length) {
        int hashcode = 0;
        for (int i = 0; i < length; i++) {
            int top = hashcode >> 24;
            hashcode += ((hashcode * 37) + top + ((int)(chars[offset++] & 0xFFFF)));
        }
        hashcode = (hashcode & 0x7fffffff);
        return (hashcode == 0) ? 1 : hashcode;
    }
    /**
     * generate partially completed character hashcode.
     * this is mean to be iterated over individual characters in order to generate
     * a full hash value
     * @see #finishHash(int)
     *
     * @param hashcode a partially completed character hashcode
     * @param ch the character to hash
     * @return a partially completed character hashcode
     */
    public static int hashChar(int hashcode, int ch) {
        int top = hashcode >> 24;
        hashcode += ((hashcode * 37) + top + ch);
        return hashcode;
    }
    /**
     * finish hashing a partically completed character hashcode
     * @see #hashChar(int,int)
     * 
     * @param hashcode a partially completed character hashcode
     * @return a character hashcode
     */
    public static int finishHash(int hashcode) {
        hashcode = (hashcode & 0x7fffffff);
        return (hashcode == 0) ? 1 : hashcode;
    }
}
