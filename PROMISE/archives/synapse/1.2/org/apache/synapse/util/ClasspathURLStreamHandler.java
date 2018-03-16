package org.apache.synapse.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.lang.StringUtils;

public final class ClasspathURLStreamHandler extends URLStreamHandler {

    public URLConnection openConnection(URL url) {
        return new URLConnectionImpl(url);
    }

    private static final class URLConnectionImpl extends URLConnection {

        public URLConnectionImpl(URL url) {
            super(url);
        }

        public void connect() {}

        public InputStream getInputStream() throws IOException {
            if (StringUtils.isNotEmpty(url.getHost())) {
                throw new MalformedURLException("No host expected in classpath URLs");
            }
            InputStream is = ClasspathURLStreamHandler.class.getClassLoader().getResourceAsStream(url.getFile());
            if (is == null) {
                throw new IOException("Classpath resource not found: " + url);
            }
            return is;
        }

        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException();
        }
    }
}
