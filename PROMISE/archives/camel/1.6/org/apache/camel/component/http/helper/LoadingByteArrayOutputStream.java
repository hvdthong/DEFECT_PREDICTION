package org.apache.camel.component.http.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Subclass of ByteArrayOutputStream that allows creation of a
 * ByteArrayInputStream directly without creating a copy of the byte[].
 *
 * Also, on "toByteArray()" it truncates it's buffer to the current size
 * and returns the new buffer directly.  Multiple calls to toByteArray()
 * will return the exact same byte[] unless a write is called in between.
 *
 * Note: once the InputStream is created, the output stream should
 * no longer be used.  In particular, make sure not to call reset()
 * and then write as that may overwrite the data that the InputStream
 * is using.
 */
public class LoadingByteArrayOutputStream extends ByteArrayOutputStream {
    public LoadingByteArrayOutputStream() {
        super(1024);
    }
    public LoadingByteArrayOutputStream(int i) {
        super(i);
    }

    public ByteArrayInputStream createInputStream() {
        return new ByteArrayInputStream(buf, 0, count);
    }

    public byte[] toByteArray() {
        if (count != buf.length) {
            buf = super.toByteArray();
        }
        return buf;
    }
}
