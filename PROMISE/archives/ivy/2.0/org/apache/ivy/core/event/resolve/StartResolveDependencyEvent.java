package org.apache.ivy.core.event.resolve;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public class StartResolveDependencyEvent extends ResolveDependencyEvent {
    public static final String NAME = "pre-resolve-dependency";

    public StartResolveDependencyEvent(
            DependencyResolver resolver, DependencyDescriptor dd, 
            ModuleRevisionId requestedRevisionId) {
        super(NAME, resolver, dd, requestedRevisionId);
    }

}
