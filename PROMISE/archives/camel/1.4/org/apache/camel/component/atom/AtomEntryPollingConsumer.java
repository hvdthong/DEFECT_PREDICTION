package org.apache.camel.component.atom;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Consumer to poll atom feeds and return each entry from the feed step by step.
 *
 * @version $Revision: 656976 $
 */
public class AtomEntryPollingConsumer extends AtomPollingConsumer {
    private Document<Feed> document;
    private int entryIndex;
    private EntryFilter entryFilter;
    private List<Entry> list;

    public AtomEntryPollingConsumer(AtomEndpoint endpoint, Processor processor, boolean filter,
                                    Date lastUpdate) {
        super(endpoint, processor);
        if (filter) {
            entryFilter = new UpdatedDateFilter(lastUpdate);
        }
    }

    public void poll() throws Exception {
        getDocument();
        Feed feed = document.getRoot();

        while (hasNextEntry()) {
            Entry entry = list.get(entryIndex--);

            boolean valid = true;
            if (entryFilter != null) {
                valid = entryFilter.isValidEntry(endpoint, document, entry);
            }
            if (valid) {
                Exchange exchange = endpoint.createExchange(feed, entry);
                getProcessor().process(exchange);
                return;
            }
        }

        document = null;
    }

    private Document<Feed> getDocument() throws IOException, ParseException {
        if (document == null) {
            document = AtomUtils.parseDocument(endpoint.getAtomUri());
            list = document.getRoot().getEntries();
            entryIndex = list.size() - 1;
        }
        return document;
    }

    private boolean hasNextEntry() {
        return entryIndex >= 0;
    }

}
