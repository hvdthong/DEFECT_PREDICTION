package org.apache.ivy.plugins.repository;

public abstract class LazyResource implements Resource {
    private boolean init = false;

    private boolean local;

    private String name;

    private long lastModified;

    private long contentLength;

    private boolean exists;

    public LazyResource(String name) {
        this.name = name;
    }

    protected abstract void init();

    private void checkInit() {
        if (!init) {
            init();
            init = true;
        }
    }

    public boolean exists() {
        checkInit();
        return exists;
    }

    public long getContentLength() {
        checkInit();
        return contentLength;
    }

    public long getLastModified() {
        checkInit();
        return lastModified;
    }

    public String getName() {
        return name;
    }

    public boolean isLocal() {
        checkInit();
        return local;
    }

    public String toString() {
        return getName();
    }

    protected void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    protected void setExists(boolean exists) {
        this.exists = exists;
    }

    protected void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    protected void setLocal(boolean local) {
        this.local = local;
    }

    protected void init(Resource r) {
        setContentLength(r.getContentLength());
        setLocal(r.isLocal());
        setLastModified(r.getLastModified());
        setExists(r.exists());
    }

}
