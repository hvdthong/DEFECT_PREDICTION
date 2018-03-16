package org.apache.ivy.plugins.circular;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.ivy.core.IvyContext;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.resolve.ResolveData;

public abstract class AbstractLogCircularDependencyStrategy 
    extends AbstractCircularDependencyStrategy {
    
    protected AbstractLogCircularDependencyStrategy(String name) {
        super(name);
    }

    private Collection/*<String>*/ circularDependencies = new HashSet();
    
    public void handleCircularDependency(ModuleRevisionId[] mrids) {
        String circularDependencyId = getCircularDependencyId(mrids);
        if (!circularDependencies.contains(circularDependencyId)) {
            circularDependencies.add(circularDependencyId);
            logCircularDependency(mrids);
        }
    }

    protected abstract void logCircularDependency(ModuleRevisionId[] mrids);

    protected String getCircularDependencyId(ModuleRevisionId[] mrids) {
        String contextPrefix = "";
        ResolveData data = IvyContext.getContext().getResolveData();
        if (data != null) {
            contextPrefix = data.getOptions().getResolveId() + " ";
        }
        return contextPrefix + Arrays.asList(mrids);
    }

}
