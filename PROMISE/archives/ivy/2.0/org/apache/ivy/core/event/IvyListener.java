package org.apache.ivy.core.event;

import java.util.EventListener;

public interface IvyListener extends EventListener {
    public void progress(IvyEvent event);
}
