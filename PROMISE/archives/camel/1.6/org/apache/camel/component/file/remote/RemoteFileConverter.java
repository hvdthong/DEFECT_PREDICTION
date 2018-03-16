package org.apache.camel.component.file.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.camel.Converter;

/**
 * A set of converter methods for working with remote file types
 *
 * @version $Revision: 680078 $
 */
@Converter
public final class RemoteFileConverter {
    
    private RemoteFileConverter() {
    }

    @Converter
    public static byte[] toByteArray(ByteArrayOutputStream os) {
        return os.toByteArray();
    }

    @Converter
    public static String toString(ByteArrayOutputStream os) {
        return os.toString();
    }

    @Converter
    public static InputStream toInputStream(ByteArrayOutputStream os) {
        return new ByteArrayInputStream(os.toByteArray());
    }

}
