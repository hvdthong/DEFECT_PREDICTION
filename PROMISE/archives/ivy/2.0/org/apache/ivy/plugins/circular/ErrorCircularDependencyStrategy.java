package org.apache.ivy.plugins.circular;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public final class ErrorCircularDependencyStrategy extends AbstractCircularDependencyStrategy {

    private static final CircularDependencyStrategy INSTANCE =
        new ErrorCircularDependencyStrategy();

    public static CircularDependencyStrategy getInstance() {
        return INSTANCE;
    }

    private ErrorCircularDependencyStrategy() {
        super("error");
    }

    public void handleCircularDependency(ModuleRevisionId[] mrids)
            throws CircularDependencyException {
        throw new CircularDependencyException(mrids);
    }
}
