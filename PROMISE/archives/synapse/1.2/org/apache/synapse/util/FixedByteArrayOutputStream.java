package org.apache.synapse.util;

import org.apache.synapse.SynapseException;
import java.io.ByteArrayOutputStream;

public class FixedByteArrayOutputStream extends ByteArrayOutputStream {

    public FixedByteArrayOutputStream(int size) {
        super(size);
    }

    public synchronized void write(int b) {
        if (count+1 > buf.length) {
            throw new SynapseException("Fixed size of internal byte array exceeded");
        }
        super.write(b);
    }

    public synchronized void write(byte b[], int off, int len) {
        if (count+len > buf.length) {
            throw new SynapseException("Fixed size of internal byte array exceeded");
        }
        super.write(b, off, len);
    }
}
