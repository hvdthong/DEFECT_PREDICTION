package org.apache.ivy.core.sort;

import org.apache.ivy.core.module.descriptor.DependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleId;
import org.apache.ivy.core.module.id.ModuleRevisionId;

abstract class MessageBasedNonMatchingVersionReporter implements NonMatchingVersionReporter {

    public void reportNonMatchingVersion(DependencyDescriptor descriptor, ModuleDescriptor md) {
        ModuleRevisionId dependencyRevisionId = descriptor.getDependencyRevisionId();
        ModuleRevisionId parentRevisionId = descriptor.getParentRevisionId();
        if (parentRevisionId == null) {
            reportMessage("Non matching revision detected when sorting.  Dependency "
                    + dependencyRevisionId + " doesn't match " + md.getModuleRevisionId());
        } else {
            ModuleId parentModuleId = parentRevisionId.getModuleId();
            reportMessage("Non matching revision detected when sorting.  " + parentModuleId
                    + " depends on " + dependencyRevisionId + ", doesn't match "
                    + md.getModuleRevisionId());
        }
    }

    protected abstract void reportMessage(String msg);

}
