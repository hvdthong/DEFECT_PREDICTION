package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

/**
 * This selector is here just to shake up your thinking a bit. Don't get
 * too caught up in boolean, there are other ways you can evaluate a
 * collection of selectors. This one takes a vote of the selectors it
 * contains, and majority wins. You could also have an "all-but-one"
 * selector, a "weighted-average" selector, and so on. These are left
 * as exercises for the reader (as are the usecases where this would
 * be necessary).
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public class MajoritySelector extends BaseSelectorContainer {

    private boolean allowtie = true;

    /**
     * Default constructor.
     */
    public MajoritySelector() {
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (hasSelectors()) {
            buf.append("{majorityselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    public void setAllowtie(boolean tiebreaker) {
        allowtie = tiebreaker;
    }

    /**
     * Returns true (the file is selected) if most of the other selectors
     * agree. In case of a tie, go by the allowtie setting. That defaults
     * to true, meaning in case of a tie, the file is selected.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file is a java.io.File object for the filename that the selector
     * can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        int yesvotes = 0;
        int novotes = 0;
        Enumeration e = selectorElements();
        boolean result;

        while(e.hasMoreElements()) {
            result = ((FileSelector)e.nextElement()).isSelected(basedir,
                    filename,file);
            if (result) {
                yesvotes = yesvotes + 1;
            }
            else {
                novotes = novotes + 1;
            }
        }
        if (yesvotes > novotes)
        {
            return true;
        }
        else if (novotes > yesvotes) {
            return false;
        }
        return allowtie;
    }
}

