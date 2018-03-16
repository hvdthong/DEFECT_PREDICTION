package org.apache.camel;


/**
 * A pluggable strategy to be able to convert objects <a
 * types</a> such as to and from String, InputStream/OutputStream,
 * Reader/Writer, Document, byte[], ByteBuffer etc
 * 
 * @version $Revision: 699004 $
 */
public interface TypeConverter {

    /**
     * Converts the value to the specified type
     * 
     * @param type the requested type
     * @param value the value to be converted
     * @return the converted value
     * @throws {@link NoTypeConversionAvailableException} if conversion not possible
     */
    <T> T convertTo(Class<T> type, Object value);

    /**
     * Converts the value to the specified type in the context of an exchange
     * <p/>
     * Used when conversion requires extra information from the current
     * exchange (such as encoding).
     *
     * @param type the requested type
     * @param exchange the current exchange
     * @param value the value to be converted
     * @return the converted value
     * @throws {@link NoTypeConversionAvailableException} if conversion not possible
     */
    <T> T convertTo(Class<T> type, Exchange exchange, Object value);
}
