package org.apache.ivy.core.resolve;

import java.util.Date;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.MetadataArtifactDownloadReport;
import org.apache.ivy.plugins.resolver.DependencyResolver;

/**
 * Represents a module revision provisioned on the local filesystem.
 */
public class ResolvedModuleRevision {
    
    private DependencyResolver resolver;

    private DependencyResolver artifactResolver;

    private ModuleDescriptor descriptor;

    private MetadataArtifactDownloadReport report;
    
    private boolean force = false;

    public ResolvedModuleRevision(DependencyResolver resolver, DependencyResolver artifactResolver,
            ModuleDescriptor descriptor, MetadataArtifactDownloadReport report) {
        this.resolver = resolver;
        this.artifactResolver = artifactResolver;
        this.descriptor = descriptor;
        this.report = report;
    }

    public ResolvedModuleRevision(DependencyResolver resolver, DependencyResolver artifactResolver,
            ModuleDescriptor descriptor, MetadataArtifactDownloadReport report, boolean force) {
        this.resolver = resolver;
        this.artifactResolver = artifactResolver;
        this.descriptor = descriptor;
        this.report = report;
        this.force = force;
    }

    /**
     * Returns the identifier of the resolved module.
     * 
     * @return the identifier of the resolved module.
     */
    public ModuleRevisionId getId() {
        return descriptor.getResolvedModuleRevisionId();
    }


    /**
     * Returns the date of publication of the resolved module.
     * 
     * @return the date of publication of the resolved module.
     */
    public Date getPublicationDate() {
        return descriptor.getResolvedPublicationDate();
    }
    

    /**
     * Returns the descriptor of the resolved module.
     * 
     * @return the descriptor of the resolved module.
     */
    public ModuleDescriptor getDescriptor() {
        return descriptor;
    }


    /**
     * The resolver which resolved this ResolvedModuleRevision
     * 
     * @return The resolver which resolved this ResolvedModuleRevision
     */
    public DependencyResolver getResolver() {
        return resolver;
    }

    /**
     * The resolver to use to download artifacts
     * 
     * @return The resolver to use to download artifacts
     */
    public DependencyResolver getArtifactResolver() {
        return artifactResolver;
    }


    /**
     * Returns a report of the resolved module metadata artifact provisioning.
     * 
     * @return a report of the resolved module metadata artifact provisioning.
     */
    public MetadataArtifactDownloadReport getReport() {
        return report;
    }
    
    /**
     * Returns <code>true</code> if this resolved module revision should be forced as the one
     * being returned.
     * <p>
     * This is used as an indication for CompositeResolver, to know if they should continue to look
     * for a better ResolvedModuleRevision if possible, or stop with this instance.
     * </p>
     * 
     * @return <code>true</code> if this resolved module revision should be forced as the one
     *         being returned.
     */
    public boolean isForce() {
        return force;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ResolvedModuleRevision)) {
            return false;
        }
        return ((ResolvedModuleRevision) obj).getId().equals(getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public String toString() {
        return getId().toString();
    }

}
