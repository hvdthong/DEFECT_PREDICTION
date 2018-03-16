package org.apache.ivy.plugins.circular;

import org.apache.ivy.core.module.id.ModuleRevisionId;

/**
 * A CircularDependencyStrategy indicates what ivy does when a circular dependency is detected.
 * Ivy can ignore it, warn the user, or interupt the processing. 
 */
public interface CircularDependencyStrategy {
    String getName();

    void handleCircularDependency(ModuleRevisionId[] mrids) throws CircularDependencyException;

}
