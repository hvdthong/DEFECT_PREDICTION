package org.apache.ivy.plugins.circular;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.util.Message;

public final class WarnCircularDependencyStrategy extends AbstractLogCircularDependencyStrategy {

    private static final CircularDependencyStrategy INSTANCE = new WarnCircularDependencyStrategy();

    public static CircularDependencyStrategy getInstance() {
        return INSTANCE;
    }

    private WarnCircularDependencyStrategy() {
        super("warn");
    }

    protected void logCircularDependency(ModuleRevisionId[] mrids) {
        Message.warn("circular dependency found: " 
            + CircularDependencyHelper.formatMessage(mrids));
    }

}
