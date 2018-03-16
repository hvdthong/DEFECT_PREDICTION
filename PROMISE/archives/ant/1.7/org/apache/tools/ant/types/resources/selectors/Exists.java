package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;

/**
 * Exists ResourceSelector.
 * @since Ant 1.7
 */
public class Exists implements ResourceSelector {

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    public boolean isSelected(Resource r) {
        return r.isExists();
    }

}
