package org.apache.ivy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ChecksumHelper {

    private static final int BUFFER_SIZE = 2048;
    private static Map algorithms = new HashMap();
    static {
        algorithms.put("md5", "MD5");
        algorithms.put("sha1", "SHA-1");
    }

    /**
     * Checks the checksum of the given file against the given checksumFile, and throws an
     * IOException if the checksum is not compliant
     * 
     * @param dest
     *            the file to test
     * @param checksumFile
     *            the file containing the expected checksum
     * @param algorithm
     *            the checksum algorithm to use
     * @throws IOException
     *             if an IO problem occur whle reading files or if the checksum is not compliant
     */
    public static void check(File dest, File checksumFile, String algorithm) throws IOException {
        String csFileContent = FileUtil.readEntirely(
            new BufferedReader(new FileReader(checksumFile))).trim().toLowerCase(Locale.US);
        String expected;
        int spaceIndex = csFileContent.indexOf(' ');
        if (spaceIndex != -1) {
            expected = csFileContent.substring(0, spaceIndex);
        } else {
            expected = csFileContent;
        }

        String computed = computeAsString(dest, algorithm).trim().toLowerCase(Locale.US);
        if (!expected.equals(computed)) {
            throw new IOException("invalid " + algorithm + ": expected=" + expected + " computed="
                    + computed);
        }
    }

    public static String computeAsString(File f, String algorithm) throws IOException {
        return byteArrayToHexString(compute(f, algorithm));
    }

    private static byte[] compute(File f, String algorithm) throws IOException {
        InputStream is = new FileInputStream(f);

        try {
            MessageDigest md = getMessageDigest(algorithm);
            md.reset();

            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                md.update(buf, 0, len);
            }
            return md.digest();
        } finally {
            is.close();
        }
    }

    private static MessageDigest getMessageDigest(String algorithm) {
        String mdAlgorithm = (String) algorithms.get(algorithm);
        if (mdAlgorithm == null) {
            throw new IllegalArgumentException("unknown algorithm " + algorithm);
        }
        try {
            return MessageDigest.getInstance(mdAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("unknown algorithm " + algorithm);
        }
    }

    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Convert a byte[] array to readable string format. This makes the "hex" readable!
     * 
     * @return result String buffer in String format
     * @param in
     *            byte[] buffer to convert to string format
     */
    public static String byteArrayToHexString(byte[] in) {
        byte ch = 0x00;

        if (in == null || in.length <= 0) {
            return null;
        }

        StringBuffer out = new StringBuffer(in.length * 2);

        for (int i = 0; i < in.length; i++) {

        }

        return out.toString();
    }

    private ChecksumHelper() {
    }
}
