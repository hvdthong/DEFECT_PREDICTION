package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;

/**
 * Interface for a Resource selector.
 * @since Ant 1.7
 */
public interface ResourceSelector {

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    boolean isSelected(Resource r);

}
