package org.apache.tools.ant.util;

/**
 * Interface for classes that want to be notified by Watchdog.
 *
 * @since Ant 1.5
 *
 * @see org.apache.tools.ant.util.Watchdog
 *
 */
public interface TimeoutObserver {

    /**
     * Called when the watchdow times out.
     *
     * @param w the watchdog that timed out.
     */
    void timeoutOccured(Watchdog w);
}
