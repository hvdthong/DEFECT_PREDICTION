package org.apache.xml.serializer;
/**
 * Holds information about a given encoding, which is the Java name for the
 * encoding, the equivalent ISO name, and the integer value of the last pritable
 * character in the encoding.
 */
public class EncodingInfo extends Object
{

    /**
     * The ISO encoding name.
     */
    final String name;

    /**
     * The name used by the Java convertor.
     */
    final String javaName;

    /**
     * The last printable character.
     */
    final int lastPrintable;

    /**
     * Create an EncodingInfo object based on the name, java name, and the
     * max character size.
     *
     * @param name non-null reference to the ISO name.
     * @param javaName non-null reference to the Java encoding name.
     * @param lastPrintable The maximum character that can be written.
     */
    public EncodingInfo(String name, String javaName, int lastPrintable)
    {

        this.name = name;
        this.javaName = javaName;
        this.lastPrintable = lastPrintable;
    }
}
