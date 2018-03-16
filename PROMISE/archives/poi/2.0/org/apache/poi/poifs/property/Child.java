package org.apache.poi.poifs.property;

/**
 * This interface defines methods for finding and setting sibling
 * Property instances
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public interface Child
{

    /**
     * Get the next Child, if any
     *
     * @return the next Child; may return null
     */

    public Child getNextChild();

    /**
     * Get the previous Child, if any
     *
     * @return the previous Child; may return null
     */

    public Child getPreviousChild();

    /**
     * Set the next Child
     *
     * @param child the new 'next' child; may be null, which has the
     *              effect of saying there is no 'next' child
     */

    public void setNextChild(final Child child);

    /**
     * Set the previous Child
     *
     * @param child the new 'previous' child; may be null, which has
     *              the effect of saying there is no 'previous' child
     */

    public void setPreviousChild(final Child child);

