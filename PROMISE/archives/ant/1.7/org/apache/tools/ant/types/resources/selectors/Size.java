package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.Comparison;

/**
 * Size ResourceSelector.
 * @since Ant 1.7
 */
public class Size implements ResourceSelector {
    private long size = -1;
    private Comparison when = Comparison.EQUAL;

    /**
     * Set the size to compare against.
     * @param l the long resource size.
     */
    public void setSize(long l) {
        size = l;
    }

    /**
     * Get the size compared to by this Size ResourceSelector.
     * @return the long resource size.
     */
    public long getSize() {
        return size;
    }

    /**
     * Set the comparison mode.
     * @param c a Comparison object.
     */
    public void setWhen(Comparison c) {
        when = c;
    }

    /**
     * Get the comparison mode.
     * @return a Comparison object.
     */
    public Comparison getWhen() {
        return when;
    }

    /**
     * Return true if this Resource is selected.
     * @param r the Resource to check.
     * @return whether the Resource was selected.
     */
    public boolean isSelected(Resource r) {
        long diff = r.getSize() - size;
        return when.evaluate(diff == 0 ? 0 : (int) (diff / Math.abs(diff)));
    }

}
