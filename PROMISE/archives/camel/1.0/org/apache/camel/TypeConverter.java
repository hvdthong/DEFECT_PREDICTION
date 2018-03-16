package org.apache.camel;

/**
 * A pluggable strategy to be able to convert objects
 * such as to and from String, InputStream/OutputStream, Reader/Writer, Document, byte[], ByteBuffer etc
 *
 * @version $Revision: 532316 $
 */
public interface TypeConverter {
    /**
     * Converts the value to the specified type
     * @param type the requested type
     * @param value the value to be converted
     * @return the converted value or null if it can not be converted
     */
    <T> T convertTo(Class<T> type, Object value);
}
