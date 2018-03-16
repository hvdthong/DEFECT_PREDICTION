package org.apache.camel.component.atom;

import java.util.Date;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filters out all entries which occur before the last time of the entry we saw (assuming
 * entries arrive sorted in order).
 *
 * @version $Revision: 656106 $
 */
public class UpdatedDateFilter implements EntryFilter {

    private static final transient Log LOG = LogFactory.getLog(UpdatedDateFilter.class);
    private Date lastUpdate;

    public UpdatedDateFilter(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isValidEntry(AtomEndpoint endpoint, Document<Feed> feed, Entry entry) {
        Date updated = entry.getUpdated();
        if (updated == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No updated time for entry so assuming its valid: entry=[" + entry + "]");
            }
            return true;
        }
        if (lastUpdate != null) {
            if (lastUpdate.after(updated)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Entry is older than lastupdate=[" + lastUpdate
                        + "], no valid entry=[" + entry + "]");
                }
                return false;
            }
        }
        lastUpdate = updated;
        return true;
    }

}
