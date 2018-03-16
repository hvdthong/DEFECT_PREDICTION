package org.apache.ivy.core.deliver;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;

/**
 *
 */
public interface PublishingDependencyRevisionResolver {

    /**
     * Returns the revision of the dependency for the publishing of the 'published' module in
     * 'publishedStatus' status.
     * 
     * @param published
     * @param publishedStatus
     * @param depMrid
     * @param status
     * @return the revision of the dependency
     */
    String resolve(ModuleDescriptor published, String publishedStatus, ModuleRevisionId depMrid, 
            String status);

}
