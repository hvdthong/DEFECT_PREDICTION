package org.apache.ivy.plugins.repository.sftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ivy.plugins.repository.Resource;

public class SFTPResource implements Resource {
    private SFTPRepository repository;

    private String path;

    private transient boolean init = false;

    private transient boolean exists;

    private transient long lastModified;

    private transient long contentLength;

    public SFTPResource(SFTPRepository repository, String path) {
        this.repository = repository;
        this.path = path;
    }

    public String getName() {
        return path;
    }

    public Resource clone(String cloneName) {
        return new SFTPResource(repository, cloneName);
    }

    public long getLastModified() {
        init();
        return lastModified;
    }

    public long getContentLength() {
        init();
        return contentLength;
    }

    public boolean exists() {
        init();
        return exists;
    }

    private void init() {
        if (!init) {
            Resource r = repository.resolveResource(path);
            contentLength = r.getContentLength();
            lastModified = r.getLastModified();
            exists = r.exists();
            init = true;
        }
    }

    public String toString() {
        return getName();
    }

    public boolean isLocal() {
        return false;
    }

    public InputStream openStream() throws IOException {
        return repository.openStream(this);
    }
}
