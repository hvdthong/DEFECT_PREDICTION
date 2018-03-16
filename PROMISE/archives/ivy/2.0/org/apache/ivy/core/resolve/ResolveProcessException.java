package org.apache.ivy.core.resolve;

/**
 * ResolveProcessException is an exception which is used to control the resolve process.
 * <p>
 * All {@link ResolveProcessException} subclasses have the ability to interrupt the resolve process
 * when thrown while resolving dependencies, instead of only marking the module with a problem and
 * continuing the resolve process as part of the best effort strategy during resolve process.
 * </p>
 * Some subclasses have even a stronger power over the resolve process, like
 * {@link RestartResolveProcess} which orders to restart the resolve process at the start.
 */
public class ResolveProcessException extends RuntimeException {

    public ResolveProcessException() {
    }

    public ResolveProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolveProcessException(String message) {
        super(message);
    }

    public ResolveProcessException(Throwable cause) {
        super(cause);
    }
}
