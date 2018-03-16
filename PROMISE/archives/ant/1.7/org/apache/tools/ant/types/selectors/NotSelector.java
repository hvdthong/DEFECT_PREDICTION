package org.apache.tools.ant.types.selectors;


/**
 * This selector has one other selectors whose meaning it inverts. It
 * actually relies on NoneSelector for its implementation of the
 * isSelected() method, but it adds a check to ensure there is only one
 * other selector contained within.
 *
 * @since 1.5
 */
public class NotSelector extends NoneSelector {

    /**
     * Default constructor.
     */
    public NotSelector() {
    }

    /**
     * Constructor that inverts the meaning of its argument.
     * @param other the selector to invert
     * @since Ant 1.7
     */
    public NotSelector(FileSelector other) {
        this();
        appendSelector(other);
    }

    /**
     * @return a string representation of the selector
     */
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
            setError("One and only one selector is allowed within the "
                + "<not> tag");
        }
    }

}

