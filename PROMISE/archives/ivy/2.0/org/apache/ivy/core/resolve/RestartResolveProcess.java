package org.apache.ivy.core.resolve;

/**
 * This RuntimeException is used during the resolve process to ask the engine to restart the resolve
 * process.
 * <p>
 * Users of this feature should be very careful to make sure they handle a termination condition,
 * since the resolve engine itself won't check the same exception is not thrown ad libitum
 * </p>
 */
public class RestartResolveProcess extends ResolveProcessException {

    public RestartResolveProcess(String message) {
        super(message);
    }

}
