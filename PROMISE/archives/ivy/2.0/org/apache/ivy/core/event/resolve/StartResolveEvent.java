package org.apache.ivy.core.event.resolve;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;

public class StartResolveEvent extends ResolveEvent {
    public static final String NAME = "pre-resolve";

    public StartResolveEvent(ModuleDescriptor md, String[] confs) {
        super(NAME, md, confs);
    }

}
