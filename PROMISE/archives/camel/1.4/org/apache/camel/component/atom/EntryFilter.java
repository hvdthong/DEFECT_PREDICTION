package org.apache.camel.component.atom;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

/**
 * Filter used by the {@link org.apache.camel.component.atom.AtomEntryPollingConsumer} to filter entries
 * from the feed.
 *
 * @version $Revision: 656106 $
 */
public interface EntryFilter {

    /**
     * Tests to be used as filtering the feed for only entries of interest, such as only new entries, etc.
     *
     * @param endpoint  the endpoint
     * @param feed      the Atom feed
     * @param entry     the given entry to filter
     * @return  <tt>true</tt> to include the entry, <ff>false</tt> to skip it
     */
    boolean isValidEntry(AtomEndpoint endpoint, Document<Feed> feed, Entry entry);

}
