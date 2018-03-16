package org.apache.ivy.util;

/**
 * Event reporting a stream copy progression
 */
public class CopyProgressEvent {
    private long totalReadBytes;

    private byte[] buffer;

    private int readBytes;

    public CopyProgressEvent() {
    }

    public CopyProgressEvent(byte[] buffer, int read, long total) {
        update(buffer, read, total);
    }

    public CopyProgressEvent(byte[] buffer, long total) {
        update(buffer, 0, total);
    }

    protected CopyProgressEvent update(byte[] buffer, int read, long total) {
        this.buffer = buffer;
        this.readBytes = read;
        this.totalReadBytes = total;
        return this;
    }

    public long getTotalReadBytes() {
        return totalReadBytes;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getReadBytes() {
        return readBytes;
    }

}
