package org.apache.tools.ant.util.optional;

import java.security.Permission;
import org.apache.tools.ant.ExitException;

/**
 * This is intended as a replacement for the default system manager.
 * The goal is to intercept System.exit calls and make it throw an
 * exception instead so that a System.exit in a task does not
 * fully terminate Ant.
 *
 * @see ExitException
 */
public class NoExitSecurityManager extends SecurityManager {

    /**
     * Override SecurityManager#checkExit.
     * This throws an ExitException(status) exception.
     * @param status the exit status
     */
    public void checkExit(int status) {
        throw new ExitException(status);
    }

    /**
     * Override SecurityManager#checkPermission.
     * This does nothing.
     * @param perm the requested permission.
     */
    public void checkPermission(Permission perm) {
    }
}
