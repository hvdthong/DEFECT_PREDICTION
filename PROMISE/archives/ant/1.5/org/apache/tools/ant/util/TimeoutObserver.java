package org.apache.tools.ant.util;

/**
 * Interface for classes that want to be notified by Watchdog.
 *
 * @since Ant 1.5
 *
 * @see org.apache.tools.ant.util.Watchdog
 *
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public interface TimeoutObserver {

    void timeoutOccured(Watchdog w);

}
