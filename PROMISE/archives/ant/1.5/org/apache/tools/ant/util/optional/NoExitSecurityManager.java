package org.apache.tools.ant.util.optional;

import org.apache.tools.ant.ExitException;

import java.security.Permission;

/**
 * This is intended as a replacement for the default system manager.
 * The goal is to intercept System.exit calls and make it throw an
 * exception instead so that a System.exit in a task does not
 * fully terminate Ant.
 *
 * @see ExitException
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 */
public class NoExitSecurityManager extends SecurityManager {

    public void checkExit(int status) {
        throw new ExitException(status);
    }

    public void checkPermission(Permission perm) {
    }
}
