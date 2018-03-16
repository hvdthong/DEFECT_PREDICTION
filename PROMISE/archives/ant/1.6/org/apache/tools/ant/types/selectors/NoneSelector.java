package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

/**
 * This selector has a collection of other selectors. All of those selectors
 * must refuse to select a file before the file is considered selected by
 * this selector.
 *
 * @since 1.5
 */
public class NoneSelector extends BaseSelectorContainer {

    /**
     * Default constructor.
     */
    public NoneSelector() {
    }

    /**
     * @return a string representation of the selector
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (hasSelectors()) {
            buf.append("{noneselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    /**
     * Returns true (the file is selected) only if all other selectors
     * agree that the file should not be selected.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file is a java.io.File object for the filename that the selector
     * can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        Enumeration e = selectorElements();
        boolean result;

        while (e.hasMoreElements()) {
            result = ((FileSelector) e.nextElement()).isSelected(basedir,
                    filename, file);
            if (result) {
                return false;
            }
        }
        return true;
    }

}

