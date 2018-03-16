package org.apache.ivy.plugins.resolver.util;

import org.apache.ivy.plugins.repository.Resource;

public interface ResourceMDParser {
    /**
     * Parses the module descriptor designed by the given resource.
     * 
     * @param resource
     *            the resource at which the module descriptor is located
     * @param rev
     *            the revision at which the module descriptor should be
     * @return the parsed module descriptor as a {@link MDResolvedResource}, or <code>null</code>
     *         if parsing has failed or if the resource is not available.
     */
    MDResolvedResource parse(Resource resource, String rev);
}
