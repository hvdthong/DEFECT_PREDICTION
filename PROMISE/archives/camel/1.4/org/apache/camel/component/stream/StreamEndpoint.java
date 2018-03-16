package org.apache.camel.component.stream;

import java.nio.charset.Charset;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamEndpoint extends DefaultEndpoint<Exchange> {
    private static final transient Log LOG = LogFactory.getLog(StreamEndpoint.class);

    private String uri;
    private String file;
    private String url;
    private long delay;
    private String encoding;

    public StreamEndpoint(String endpointUri, Component component) throws Exception {
        super(endpointUri, component);
        this.uri = endpointUri;
    }

    public StreamEndpoint(String endpointUri) {
        super(endpointUri);
        this.uri = endpointUri;
    }

    public Consumer<Exchange> createConsumer(Processor processor) throws Exception {
        return new StreamConsumer(this, processor, uri);
    }

    public Producer<Exchange> createProducer() throws Exception {
        return new StreamProducer(this, uri);
    }

    public boolean isSingleton() {
        return true;
    }


    public String getFile() {
        return file;
    }

    /**
     * @deprecated use camel-file component. Will be removed in Camel 2.0
     */
    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    /**
     * @deprecated use camel-jetty or camel-http component. Will be removed in Camel 2.0
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    Charset getCharset() {
        if (encoding == null) {
            encoding = Charset.defaultCharset().name();
            if (LOG.isDebugEnabled()) {
                LOG.debug("No encoding parameter using default charset: " + encoding);
            }
        }
        if (!Charset.isSupported(encoding)) {
            throw new IllegalArgumentException("The encoding: " + encoding + " is not supported");
        }

        return Charset.forName(encoding);
    }

}
