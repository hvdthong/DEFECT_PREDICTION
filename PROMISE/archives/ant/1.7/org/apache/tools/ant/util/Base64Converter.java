package org.apache.tools.ant.util;

/**
 * BASE 64 encoding of a String or an array of bytes.
 *
 * Based on RFC 1421.
 *
 **/
public class Base64Converter {

    private static final int BYTE      = 8;
    private static final int WORD      = 16;
    private static final int BYTE_MASK = 0xFF;
    private static final int POS_0_MASK = 0x0000003F;
    private static final int POS_1_MASK = 0x00000FC0;
    private static final int POS_1_SHIFT = 6;
    private static final int POS_2_MASK = 0x0003F000;
    private static final int POS_2_SHIFT = 12;
    private static final int POS_3_MASK = 0x00FC0000;
    private static final int POS_3_SHIFT = 18;


    private static final char[] ALPHABET = {

    /** Provided for BC purposes */
    public static final char[] alphabet = ALPHABET;


    /**
     * Encode a string into base64 encoding.
     * @param s the string to encode.
     * @return the encoded string.
     */
    public String encode(String s) {
        return encode(s.getBytes());
    }

    /**
     * Encode a byte array into base64 encoding.
     * @param octetString the byte array to encode.
     * @return the encoded string.
     */
    public String encode(byte[] octetString) {
        int bits24;
        int bits6;

        char[] out = new char[((octetString.length - 1) / 3 + 1) * 4];
        int outIndex = 0;
        int i = 0;

        while ((i + 3) <= octetString.length) {
            bits24 = (octetString[i++] & BYTE_MASK) << WORD;
            bits24 |= (octetString[i++] & BYTE_MASK) << BYTE;
            bits24 |= octetString[i++];

            bits6 = (bits24 & POS_3_MASK) >> POS_3_SHIFT;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & POS_2_MASK) >> POS_2_SHIFT;
            out[outIndex++] = ALPHABET[bits6];
            bits6  = (bits24 & POS_1_MASK) >> POS_1_SHIFT;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & POS_0_MASK);
            out[outIndex++] = ALPHABET[bits6];
        }
        if (octetString.length - i == 2) {
            bits24 = (octetString[i] & BYTE_MASK) << WORD;
            bits24 |= (octetString[i + 1] & BYTE_MASK) << BYTE;
            bits6 = (bits24 & POS_3_MASK) >> POS_3_SHIFT;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & POS_2_MASK) >> POS_2_SHIFT;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & POS_1_MASK) >> POS_1_SHIFT;
            out[outIndex++] = ALPHABET[bits6];

            out[outIndex++] = '=';
        } else if (octetString.length - i == 1) {
            bits24 = (octetString[i] & BYTE_MASK) << WORD;
            bits6 = (bits24 & POS_3_MASK) >> POS_3_SHIFT;
            out[outIndex++] = ALPHABET[bits6];
            bits6 = (bits24 & POS_2_MASK) >> POS_2_SHIFT;
            out[outIndex++] = ALPHABET[bits6];

            out[outIndex++] = '=';
            out[outIndex++] = '=';
        }
        return new String(out);
    }
}
