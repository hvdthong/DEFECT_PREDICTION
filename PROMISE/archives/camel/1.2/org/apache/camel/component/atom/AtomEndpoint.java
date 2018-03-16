package org.apache.camel.component.atom;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.apache.abdera.util.iri.IRISyntaxException;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Producer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultPollingEndpoint;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 *
 * @version $Revision: 1.1 $
 */
public class AtomEndpoint extends DefaultPollingEndpoint {
    private Factory atomFactory;
    private String atomUri;

    public AtomEndpoint(String endpointUri, AtomComponent component, String atomUri) {
        super(endpointUri, component);
        this.atomUri = atomUri;
    }

    public boolean isSingleton() {
        return true;
    }

    public Producer createProducer() throws Exception {
        return new AtomProducer(this);
    }

    @Override
    public PollingConsumer createPollingConsumer() throws Exception {
        return new AtomPollingConsumer(this);
    }

    public Document<Feed> parseDocument() throws IRISyntaxException, IOException {
        String uri = getAtomUri();
        InputStream in = new URL(uri).openStream();
        return createAtomParser().parse(in, uri);
    }

    public OutputStream createProducerOutputStream() throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(getAtomUri()));
    }

    public Factory getAtomFactory() {
        if (atomFactory == null) {
            atomFactory = createAtomFactory();
        }
        return atomFactory;
    }

    public void setAtomFactory(Factory atomFactory) {
        this.atomFactory = atomFactory;
    }

    public String getAtomUri() {
        return atomUri;
    }

    public void setAtomUri(String atomUri) {
        this.atomUri = atomUri;
    }

    protected Factory createAtomFactory() {
        return Abdera.getNewFactory();
    }

    protected Parser createAtomParser() {
        return Abdera.getNewParser();
    }
}
