package org.apache.ivy.core.cache;


public class CacheDownloadOptions {
    private DownloadListener listener = null;
    private boolean force = false;

    public DownloadListener getListener() {
        return listener;
    }
    public CacheDownloadOptions setListener(DownloadListener listener) {
        this.listener = listener;
        return this;
    }
    public boolean isForce() {
        return force;
    }
    public CacheDownloadOptions setForce(boolean force) {
        this.force = force;
        return this;
    }
}
