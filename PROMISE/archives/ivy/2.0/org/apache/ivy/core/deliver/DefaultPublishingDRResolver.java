package org.apache.ivy.core.deliver;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

public class DefaultPublishingDRResolver implements PublishingDependencyRevisionResolver {
    public String resolve(ModuleDescriptor published, String publishedStatus,
            ModuleRevisionId depMrid, String status) {
        return depMrid.getRevision();
    }
}
