package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;

import org.apache.tools.ant.types.Resource;

/**
 * None ResourceSelector.
 * @since Ant 1.7
 */
public class None
    extends ResourceSelectorContainer implements ResourceSelector {

    /**
     * Default constructor.
     */
    public None() {
    }

    /**
     * Convenience constructor.
     * @param r the ResourceSelector[] to add.
     */
    public None(ResourceSelector[] r) {
        super(r);
    }

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    public boolean isSelected(Resource r) {
        boolean none = true;
        for (Iterator i = getSelectors(); none && i.hasNext();) {
            none = !((ResourceSelector) i.next()).isSelected(r);
        }
        return none;
    }

}
