package org.apache.poi.poifs.property;

import java.util.Iterator;

import java.io.IOException;

/**
 * Behavior for parent (directory) properties
 *
 * @author Marc Johnson27591@hotmail.com
 */

public interface Parent
    extends Child
{

    /**
     * Get an iterator over the children of this Parent; all elements
     * are instances of Property.
     *
     * @return Iterator of children; may refer to an empty collection
     */

    public Iterator getChildren();

    /**
     * Add a new child to the collection of children
     *
     * @param property the new child to be added; must not be null
     *
     * @exception IOException if the Parent already has a child with
     *                        the same name
     */

    public void addChild(final Property property)
        throws IOException;

    /**
     * Set the previous Child
     *
     * @param child the new 'previous' child; may be null, which has
     *              the effect of saying there is no 'previous' child
     */

    public void setPreviousChild(final Child child);

    /**
     * Set the next Child
     *
     * @param child the new 'next' child; may be null, which has the
     *              effect of saying there is no 'next' child
     */

    public void setNextChild(final Child child);

    /** *** end methods from interface Child *** */


