package org.apache.ivy.plugins.repository.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.util.url.URLHandlerRegistry;
import org.apache.ivy.util.url.URLHandler.URLInfo;

public class URLResource implements Resource {
    private URL url;

    private boolean init = false;

    private long lastModified;

    private long contentLength;

    private boolean exists;

    public URLResource(URL url) {
        this.url = url;
    }

    public String getName() {
        return url.toExternalForm();
    }

    public Resource clone(String cloneName) {
        try {
            return new URLResource(new URL(cloneName));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "bad clone name provided: not suitable for an URLResource: " + cloneName);
        }
    }

    public long getLastModified() {
        if (!init) {
            init();
        }
        return lastModified;
    }

    private void init() {
        URLInfo info = URLHandlerRegistry.getDefault().getURLInfo(url);
        contentLength = info.getContentLength();
        lastModified = info.getLastModified();
        exists = info.isReachable();
        init = true;
    }

    public long getContentLength() {
        if (!init) {
            init();
        }
        return contentLength;
    }

    public boolean exists() {
        if (!init) {
            init();
        }
        return exists;
    }

    public URL getURL() {
        return url;
    }

    public String toString() {
        return getName();
    }

    public boolean isLocal() {
        return false;
    }

    public InputStream openStream() throws IOException {
        return url.openStream();
    }
}
