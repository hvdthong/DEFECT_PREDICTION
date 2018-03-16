package org.apache.ivy.plugins.circular;

import org.apache.ivy.core.module.id.ModuleRevisionId;

/**
 * Unchecked exception thrown when a circular dependency exists between projects.
 */

public class CircularDependencyException extends RuntimeException {

    private ModuleRevisionId[] mrids;

    /**
     * @param descriptors
     *            module descriptors in order of circular dependency
     */
    public CircularDependencyException(final ModuleRevisionId[] mrids) {
        super(CircularDependencyHelper.formatMessage(mrids));
        this.mrids = mrids;
    }

    public ModuleRevisionId[] getPath() {
        return this.mrids;
    }

}
