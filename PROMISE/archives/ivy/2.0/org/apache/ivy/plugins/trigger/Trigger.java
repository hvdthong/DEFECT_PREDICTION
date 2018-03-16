package org.apache.ivy.plugins.trigger;

import org.apache.ivy.core.event.IvyListener;
import org.apache.ivy.util.filter.Filter;

public interface Trigger extends IvyListener {
    Filter getEventFilter();
}
