package org.apache.ivy.plugins.conflict;

import java.util.Arrays;

import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.core.resolve.ResolveProcessException;

public class StrictConflictException extends ResolveProcessException {

    public StrictConflictException() {
        super();
    }

    public StrictConflictException(IvyNode node1, IvyNode node2) {
        super(node1 + " (needed by " + Arrays.asList(node1.getAllRealCallers())
            + ") conflicts with " + node2 + " (needed by "
            + Arrays.asList(node2.getAllRealCallers()) + ")");
    }

    public StrictConflictException(String msg) {
        super(msg);
    }

    public StrictConflictException(Throwable t) {
        super(t);
    }

    public StrictConflictException(String msg, Throwable t) {
        super(msg, t);
    }

}
