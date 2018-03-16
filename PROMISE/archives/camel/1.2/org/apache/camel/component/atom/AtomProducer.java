package org.apache.camel.component.atom;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.util.iri.IRISyntaxException;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import static org.apache.camel.util.ExchangeHelper.getExchangeProperty;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @version $Revision: 1.1 $
 */
public class AtomProducer extends DefaultProducer {
    private static final transient Log LOG = LogFactory.getLog(AtomProducer.class);
    private final AtomEndpoint endpoint;

    public AtomProducer(AtomEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        Document<Feed> document = getDocument(exchange);

        OutputStream out = endpoint.createProducerOutputStream();
        try {
            document.writeTo(out);
        }
        finally {
            ObjectHelper.close(out, "Atom document output stream", LOG);
        }
    }

    protected Document<Feed> getDocument(Exchange exchange) throws IRISyntaxException, IOException {
        Document<Feed> document = endpoint.parseDocument();
        Feed root = document.getRoot();
        Entry entry = root.addEntry();
        entry.setPublished(org.apache.camel.util.ExchangeHelper.getExchangeProperty(exchange, "org.apache.camel.atom.published", Date.class, new Date()));

        String id = exchange.getProperty("org.apache.camel.atom.id", String.class);
        if (id != null) {
            entry.setId(id);
        }
        String content = exchange.getProperty("org.apache.camel.atom.content", String.class);
        if (content != null) {
            entry.setContent(content);
        }
        String summary = exchange.getProperty("org.apache.camel.atom.summary", String.class);
        if (summary != null) {
            entry.setSummary(summary);
        }
        String title = exchange.getProperty("org.apache.camel.atom.title", String.class);
        if (title != null) {
            entry.setTitle(title);
        }
        return document;
    }
}
