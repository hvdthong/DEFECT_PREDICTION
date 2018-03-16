package org.apache.ivy.core.module.descriptor;

/**
 * A  DependencyDescriptorMediator is responsible for dependency descriptor mediation.
 * <p>
 * Dependency descriptor mediation consists in adjusting dependency descriptors according to
 * a context, environment, the stack of dependers, ...
 * </p> 
 */
public interface DependencyDescriptorMediator {
    
    /**
     * Mediates the given {@link DependencyDescriptor} according to this {@link ModuleDescriptor}.
     * <p>
     * This method gives the opportunity to a ModuleDescriptor to override dependency version
     * information of any of its transitive dependencies, since it is called by dependency resolvers
     * before actually resolving a dependency.
     * </p>
     * 
     * @param dd
     *            the dependency descriptor which should be mediated.
     * @return the mediated {@link DependencyDescriptor}, or the original
     *         {@link DependencyDescriptor} if no mediation is required by this ModuleDescriptor.
     */
    DependencyDescriptor mediate(DependencyDescriptor dd);

}
