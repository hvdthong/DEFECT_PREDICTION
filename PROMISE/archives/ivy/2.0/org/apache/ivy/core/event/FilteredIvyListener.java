package org.apache.ivy.core.event;

import org.apache.ivy.util.filter.Filter;

public class FilteredIvyListener implements IvyListener {
    private IvyListener listener;

    private Filter filter;

    public FilteredIvyListener(IvyListener listener, Filter filter) {
        this.listener = listener;
        this.filter = filter;
    }

    public IvyListener getIvyListener() {
        return listener;
    }

    public Filter getFilter() {
        return filter;
    }

    public void progress(IvyEvent event) {
        if (filter.accept(event)) {
            listener.progress(event);
        }
    }

}
