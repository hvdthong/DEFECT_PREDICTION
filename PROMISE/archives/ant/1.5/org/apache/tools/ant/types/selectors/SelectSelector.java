package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import  org.apache.tools.ant.types.Reference;

/**
 * This selector just holds one other selector and forwards all
 * requests to it. It exists so that there is a single selector
 * type that can exist outside of any targets, as an element of
 * project. It overrides all of the reference stuff so that it
 * works as expected. Note that this is the only selector you
 * can reference.
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public class SelectSelector extends AndSelector {

    /**
     * Default constructor.
     */
    public SelectSelector() {
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (hasSelectors()) {
            buf.append("{select: ");
            buf.append(super.toString());
            buf.append("}");
        }
        return buf.toString();
    }

    /**
     * Performs the check for circular references and returns the
     * referenced Selector.
     */
    private SelectSelector getRef() {
        Object o = getCheckedRef(this.getClass(), "SelectSelector");
        return (SelectSelector) o;
    }

    /**
     * Indicates whether there are any selectors here.
     */
    public boolean hasSelectors() {
        if (isReference()) {
            return getRef().hasSelectors();
        }
        return super.hasSelectors();
    }

    /**
     * Gives the count of the number of selectors in this container
     */
    public int selectorCount() {
        if (isReference()) {
            return getRef().selectorCount();
        }
        return super.selectorCount();
    }

    /**
     * Returns the set of selectors as an array.
     */
    public FileSelector[] getSelectors(Project p) {
        if (isReference()) {
            return getRef().getSelectors(p);
        }
        return super.getSelectors(p);
    }

    /**
     * Returns an enumerator for accessing the set of selectors.
     */
    public Enumeration selectorElements() {
        if (isReference()) {
            return getRef().selectorElements();
        }
        return super.selectorElements();
    }

    /**
     * Add a new selector into this container.
     *
     * @param selector the new selector to add
     * @return the selector that was added
     */
    public void appendSelector(FileSelector selector) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        super.appendSelector(selector);
    }



    /**
     * Makes sure that there is only one entry, sets an error message if
     * not.
     */
    public void verifySettings() {
        if (selectorCount() != 1) {
            setError("One and only one selector is allowed within the " +
                    "<selector> tag");
        }
    }

}

