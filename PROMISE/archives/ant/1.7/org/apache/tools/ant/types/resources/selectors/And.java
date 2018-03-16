package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;

import org.apache.tools.ant.types.Resource;

/**
 * And ResourceSelector.
 * @since Ant 1.7
 */
public class And extends ResourceSelectorContainer implements ResourceSelector {

    /**
     * Default constructor.
     */
    public And() {
    }

    /**
     * Convenience constructor.
     * @param r the ResourceSelector[] to add.
     */
    public And(ResourceSelector[] r) {
        super(r);
    }

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    public boolean isSelected(Resource r) {
        for (Iterator i = getSelectors(); i.hasNext();) {
            if (!((ResourceSelector) i.next()).isSelected(r)) {
                return false;
            }
        }
        return true;
    }

}
