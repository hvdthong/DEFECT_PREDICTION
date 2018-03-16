package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import  org.apache.tools.ant.types.Reference;

import java.io.File;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

/**
 * This is the interface for selectors that can contain other selectors.
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public interface SelectorContainer {

    /**
     * Indicates whether there are any selectors here.
     *
     * @return whether any selectors are in this container
     */
    public boolean hasSelectors();

    /**
     * Gives the count of the number of selectors in this container
     *
     * @return the number of selectors in this container
     */
    public int selectorCount();

    /**
     * Returns the set of selectors as an array.
     *
     * @return an array of selectors in this container
     */
    public FileSelector[] getSelectors(Project p);

    /**
     * Returns an enumerator for accessing the set of selectors.
     *
     * @return an enumerator that goes through each of the selectors
     */
    public Enumeration selectorElements();

    /**
     * Add a new selector into this container.
     *
     * @param selector the new selector to add
     * @return the selector that was added
     */
    public void appendSelector(FileSelector selector);

    /* Methods below all add specific selectors */

    /**
     * add a "Select" selector entry on the selector list
     */
    public void addSelector(SelectSelector selector);

    /**
     * add an "And" selector entry on the selector list
     */
    public void addAnd(AndSelector selector);

    /**
     * add an "Or" selector entry on the selector list
     */
    public void addOr(OrSelector selector);

    /**
     * add a "Not" selector entry on the selector list
     */
    public void addNot(NotSelector selector);

    /**
     * add a "None" selector entry on the selector list
     */
    public void addNone(NoneSelector selector);

    /**
     * add a majority selector entry on the selector list
     */
    public void addMajority(MajoritySelector selector);

    /**
     * add a selector date entry on the selector list
     */
    public void addDate(DateSelector selector);

    /**
     * add a selector size entry on the selector list
     */
    public void addSize(SizeSelector selector);

    /**
     * add a selector filename entry on the selector list
     */
    public void addFilename(FilenameSelector selector);

    /**
     * add an extended selector entry on the selector list
     */
    public void addCustom(ExtendSelector selector);

    /**
     * add a contains selector entry on the selector list
     */
    public void addContains(ContainsSelector selector);

    /**
     * add a present selector entry on the selector list
     */
    public void addPresent(PresentSelector selector);

    /**
     * add a depth selector entry on the selector list
     */
    public void addDepth(DepthSelector selector);

    /**
     * add a depends selector entry on the selector list
     */
    public void addDepend(DependSelector selector);

}

