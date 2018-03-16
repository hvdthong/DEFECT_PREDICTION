package fr.jayasoft.ivy.event;

import java.util.EventListener;

public interface IvyListener extends EventListener {
    public void progress(IvyEvent event);
}
