package org.apache.ivy.core.event.download;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.plugins.resolver.DependencyResolver;

public class NeedArtifactEvent extends DownloadEvent {
    public static final String NAME = "need-artifact";

    private DependencyResolver resolver;

    public NeedArtifactEvent(DependencyResolver resolver, Artifact artifact) {
        super(NAME, artifact);
        this.resolver = resolver;
        addAttribute("resolver", this.resolver.getName());
    }

    public DependencyResolver getResolver() {
        return resolver;
    }

}
