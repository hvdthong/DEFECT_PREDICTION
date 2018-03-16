package org.apache.camel.component.mina;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.converter.IOConverter;
import org.apache.mina.common.ByteBuffer;

/**
 * A set of converter methods for working with MINA types
 *
 * @version $Revision: 690920 $
 */
@Converter
public final class MinaConverter {
    private MinaConverter() {
    }
    @Converter
    public static byte[] toByteArray(ByteBuffer buffer) {
        byte[] answer = new byte[buffer.remaining()];
        try {
            buffer.acquire();
        } catch (IllegalStateException ex) {
        }
        buffer.get(answer);
        return answer;
    }

    @Converter
    public static String toString(ByteBuffer buffer, Exchange exchange) {
        return IOConverter.toString(toByteArray(buffer), exchange);
    }

    @Converter
    public static InputStream toInputStream(ByteBuffer buffer) {
        return buffer.asInputStream();
    }

    @Converter
    public static ObjectInput toObjectInput(ByteBuffer buffer) throws IOException {
        return IOConverter.toObjectInput(toInputStream(buffer));
    }

    @Converter
    public static ByteBuffer toByteBuffer(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        buf.put(bytes);
        return buf;
    }
}
