package org.apache.ivy.core.event.resolve;

import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public class ResolveDependencyEvent extends IvyEvent {
    private DependencyResolver resolver;

    private DependencyDescriptor dd;

    protected ResolveDependencyEvent(String name, DependencyResolver resolver,
            DependencyDescriptor dd, ModuleRevisionId requestedRevisionId) {
        super(name);
        this.resolver = resolver;
        this.dd = dd;
        addAttribute("resolver", this.resolver.getName());
        addMridAttributes(this.dd.getDependencyRevisionId());
        addAttributes(this.dd.getQualifiedExtraAttributes());
        addAttributes(this.dd.getExtraAttributes());
        addAttribute("req-revision", requestedRevisionId.getRevision());
        addAttribute("req-revision-default", 
            dd.getDependencyRevisionId().getRevision());
        addAttribute("req-revision-dynamic", 
            dd.getDynamicConstraintDependencyRevisionId().getRevision());
        addAttribute("req-branch", requestedRevisionId.getBranch());
        addAttribute("req-branch-default", dd.getDependencyRevisionId().getBranch());
    }

    public DependencyDescriptor getDependencyDescriptor() {
        return dd;
    }

    public DependencyResolver getResolver() {
        return resolver;
    }

}
