package org.apache.ivy.plugins.resolver.util;

import org.apache.ivy.core.resolve.ResolvedModuleRevision;
import org.apache.ivy.plugins.repository.Resource;

public class MDResolvedResource extends ResolvedResource {
    private ResolvedModuleRevision rmr;

    public MDResolvedResource(Resource res, String rev, ResolvedModuleRevision rmr) {
        super(res, rev);
        this.rmr = rmr;
    }

    public ResolvedModuleRevision getResolvedModuleRevision() {
        return rmr;
    }

}
