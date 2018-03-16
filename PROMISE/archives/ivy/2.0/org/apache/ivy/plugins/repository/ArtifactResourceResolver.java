package org.apache.ivy.plugins.repository;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.plugins.resolver.util.ResolvedResource;

/**
 * An {@link ArtifactResourceResolver} is responsible for the resolution of an artifact into a
 * {@link ResolvedResource}.
 */
public interface ArtifactResourceResolver {
    public ResolvedResource resolve(Artifact artifact);
}
