package org.apache.poi.poifs.dev;

import java.util.Iterator;

/**
 * Interface for a drill-down viewable object. Such an object has
 * content that may or may not be displayed, at the discretion of the
 * viewer. The content is returned to the viewer as an array or as an
 * Iterator, and the object provides a clue as to which technique the
 * viewer should use to get its content.
 *
 * A POIFSViewable object is also expected to provide a short
 * description of itself, that can be used by a viewer when the
 * viewable object is collapsed.
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public interface POIFSViewable
{

    /**
     * Get an array of objects, some of which may implement
     * POIFSViewable
     *
     * @return an array of Object; may not be null, but may be empty
     */

    public Object [] getViewableArray();

    /**
     * Get an Iterator of objects, some of which may implement
     * POIFSViewable
     *
     * @return an Iterator; may not be null, but may have an empty
     * back end store
     */

    public Iterator getViewableIterator();

    /**
     * Give viewers a hint as to whether to call getViewableArray or
     * getViewableIterator
     *
     * @return true if a viewer should call getViewableArray, false if
     *         a viewer should call getViewableIterator
     */

    public boolean preferArray();

    /**
     * Provides a short description of the object, to be used when a
     * POIFSViewable object has not provided its contents.
     *
     * @return short description
     */

    public String getShortDescription();

