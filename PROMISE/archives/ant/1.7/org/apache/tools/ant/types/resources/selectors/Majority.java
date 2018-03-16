package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;

import org.apache.tools.ant.types.Resource;

/**
 * Majority ResourceSelector.
 * @since Ant 1.7
 */
public class Majority
    extends ResourceSelectorContainer implements ResourceSelector {

    private boolean tie = true;

    /**
     * Default constructor.
     */
    public Majority() {
    }

    /**
     * Convenience constructor.
     * @param r the ResourceSelector[] to add.
     */
    public Majority(ResourceSelector[] r) {
        super(r);
    }

    /**
     * Set whether ties are allowed.
     * @param b whether a tie is a pass.
     */
    public synchronized void setAllowtie(boolean b) {
        tie = b;
    }

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    public synchronized boolean isSelected(Resource r) {
        int passed = 0;
        int failed = 0;
        int count = selectorCount();
        boolean even = count % 2 == 0;
        int threshold = count / 2;

        for (Iterator i = getSelectors(); i.hasNext();) {
            if (((ResourceSelector) i.next()).isSelected(r)) {
                ++passed;
                if (passed > threshold || (even && tie && passed == threshold)) {
                    return true;
                }
            } else {
                ++failed;
                if (failed > threshold || (even && !tie && failed == threshold)) {
                    return false;
                }
            }
        }
        return false;
    }

}
