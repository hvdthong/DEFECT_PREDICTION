package org.apache.camel.converter;

import org.apache.camel.Converter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Some core java.nio based 
 *
 * @version $Revision: 564495 $
 */
@Converter
public class NIOConverter {

    /**
     * Utility classes should not have a public constructor.
     */
    private NIOConverter() {        
    }

    @Converter
    public static byte[] toByteArray(ByteBuffer buffer) {
        return buffer.array();
    }

    @Converter
    public static String toString(ByteBuffer buffer) {
        return IOConverter.toString(buffer.array());
    }

    @Converter
    public static ByteBuffer toByteBuffer(byte[] data) {
        return ByteBuffer.wrap(data);
    }
    
    @Converter
    public static ByteBuffer toByteBuffer(File file) throws IOException {
       byte[] buf = new byte[(int) file.length()];
       InputStream in = new BufferedInputStream(new FileInputStream(file));
       in.read(buf);
       return ByteBuffer.wrap(buf);
    }

    @Converter
    public static ByteBuffer toByteBuffer(String value) {
        ByteBuffer buf = ByteBuffer.allocate(value.length());
        byte[] bytes = value.getBytes();
        buf.put(bytes);
        return buf;
    }
    @Converter
    public static ByteBuffer toByteBuffer(Short value) {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort(value);
        return buf;
    }
    @Converter
    public static ByteBuffer toByteBuffer(Integer value) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(value);
        return buf;
    }
    @Converter
    public static ByteBuffer toByteBuffer(Long value) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong(value);
        return buf;
    }
    @Converter
    public static ByteBuffer toByteBuffer(Float value) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putFloat(value);
        return buf;
    }
    @Converter
    public static ByteBuffer toByteBuffer(Double value) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putDouble(value);
        return buf;
    }

    @Converter
    public static InputStream toInputStream(ByteBuffer bufferbuffer) {
        return IOConverter.toInputStream(toByteArray(bufferbuffer));
    }
}
