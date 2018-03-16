package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

/**
 * This selector has a collection of other selectors, any of which have to
 * select a file in order for this selector to select it.
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public class OrSelector extends BaseSelectorContainer {

    /**
     * Default constructor.
     */
    public OrSelector() {
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (hasSelectors()) {
            buf.append("{orselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    /**
     * Returns true (the file is selected) if any of the other selectors
     * agree that the file should be selected.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename the name of the file to check
     * @param file a java.io.File object for the filename that the selector
     * can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        Enumeration e = selectorElements();
        boolean result;

        while(e.hasMoreElements()) {
            result = ((FileSelector)e.nextElement()).isSelected(basedir,
                    filename,file);
            if (result) {
                return true;
            }
        }
        return false;
    }

}

