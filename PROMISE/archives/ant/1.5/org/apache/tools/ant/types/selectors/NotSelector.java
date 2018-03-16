package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

/**
 * This selector has one other selectors whose meaning it inverts. It
 * actually relies on NoneSelector for its implementation of the
 * isSelected() method, but it adds a check to ensure there is only one
 * other selector contained within.
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public class NotSelector extends NoneSelector {

    /**
     * Default constructor.
     */
    public NotSelector() {
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (hasSelectors()) {
            buf.append("{notselect: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    /**
     * Makes sure that there is only one entry, sets an error message if
     * not.
     */
    public void verifySettings() {
        if (selectorCount() != 1) {
            setError("One and only one selector is allowed within the " +
                    "<not> tag");
        }
    }

}

