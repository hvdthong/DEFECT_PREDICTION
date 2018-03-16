package org.apache.ivy.core.module.descriptor;

import org.apache.ivy.core.module.id.ModuleRevisionId;

/**
 * DependencyDescriptorMediator used to override some dependency descriptors values, such as 
 * the branch or version of the dependency. 
 */
public class OverrideDependencyDescriptorMediator implements DependencyDescriptorMediator {
    private String version;
    private String branch;
    
    /**
     * Constructs a new instance.
     * 
     * @param branch
     *            the branch to give to mediated dependency descriptors, <code>null</code> to keep
     *            the original branch.
     * @param version
     *            the version to give to mediated dependency descriptors, <code>null</code> to
     *            keep the original one.
     */
    public OverrideDependencyDescriptorMediator(String branch, String version) {
        this.branch = branch;
        this.version = version;
    }

    /**
     * Returns the version this mediator will give to mediated descriptors, or <code>null</code>
     * if this mediator does not override version.
     * 
     * @return the version this mediator will give to mediated descriptors.
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Returns the branch this mediator will give to mediated descriptors, or <code>null</code>
     * if this mediator does not override branch.
     * 
     * @return the branch this mediator will give to mediated descriptors.
     */
    public String getBranch() {
        return branch;
    }

    public DependencyDescriptor mediate(DependencyDescriptor dd) {
        ModuleRevisionId mrid = dd.getDependencyRevisionId();
        if ((version == null || version.equals(mrid.getRevision()))
                && (branch == null || branch.equals(mrid.getBranch()))) {
            return dd;
        }
        
        String version = this.version == null ? mrid.getRevision() : this.version;
        String branch = this.branch == null ? mrid.getBranch() : this.branch;
        
        return dd.clone(ModuleRevisionId.newInstance(
            mrid.getOrganisation(), mrid.getName(), branch, version, 
            mrid.getQualifiedExtraAttributes()));
    }
}
