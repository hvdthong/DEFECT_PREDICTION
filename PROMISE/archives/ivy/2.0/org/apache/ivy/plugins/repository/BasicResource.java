package org.apache.ivy.plugins.repository;

import java.io.IOException;
import java.io.InputStream;

public class BasicResource implements Resource {
    private boolean local;

    private String name;

    private long lastModified;

    private long contentLength;

    private boolean exists;

    public BasicResource(String name, boolean exists, long contentLength, long lastModified,
            boolean local) {
        this.name = name;
        this.exists = exists;
        this.contentLength = contentLength;
        this.lastModified = lastModified;
        this.local = local;
    }

    public Resource clone(String cloneName) {
        throw new UnsupportedOperationException("basic resource do not support the clone method");
    }

    public boolean exists() {
        return this.exists;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public String getName() {
        return this.name;
    }

    public boolean isLocal() {
        return this.local;
    }

    public InputStream openStream() throws IOException {
        throw new UnsupportedOperationException(
                "basic resource do not support the openStream method");
    }

    public String toString() {
        return getName();
    }

}
