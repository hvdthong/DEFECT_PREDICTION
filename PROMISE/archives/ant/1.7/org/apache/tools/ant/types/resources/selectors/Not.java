package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;

/**
 * Not ResourceSelector.
 * @since Ant 1.7
 */
public class Not implements ResourceSelector {

    private ResourceSelector sel;

    /**
     * Default constructor.
     */
    public Not() {
    }

    /**
     * Convenience constructor.
     * @param s the ResourceSelector to negate.
     */
    public Not(ResourceSelector s) {
        add(s);
    }

    /**
     * Set the ResourceSelector.
     * @param s the ResourceSelector to negate.
     * @throws IllegalStateException if already set.
     */
    public void add(ResourceSelector s) {
        if (sel != null) {
            throw new IllegalStateException(
                "The Not ResourceSelector accepts a single nested ResourceSelector");
        }
        sel = s;
    }

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    public boolean isSelected(Resource r) {
        return !(sel.isSelected(r));
    }

}
