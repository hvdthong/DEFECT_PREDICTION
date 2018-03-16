package org.apache.ivy.core.event.resolve;

import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;

public class ResolveEvent extends IvyEvent {
    private ModuleDescriptor md;

    protected ResolveEvent(String name, ModuleDescriptor md, String[] confs) {
        super(name);
        this.md = md;
        addMDAttributes(md);
        addConfsAttribute(confs);
    }

    public ModuleDescriptor getModuleDescriptor() {
        return md;
    }
}
