package org.apache.ivy.plugins.circular;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.util.Message;

public final class IgnoreCircularDependencyStrategy extends AbstractLogCircularDependencyStrategy {

    private static final CircularDependencyStrategy INSTANCE = 
           new IgnoreCircularDependencyStrategy();

    public static CircularDependencyStrategy getInstance() {
        return INSTANCE;
    }

    private IgnoreCircularDependencyStrategy() {
        super("warn");
    }

    protected void logCircularDependency(ModuleRevisionId[] mrids) {
        Message.verbose("circular dependency found: " 
            + CircularDependencyHelper.formatMessage(mrids));
    }

}

