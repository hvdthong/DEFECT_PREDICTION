package org.apache.poi.poifs.filesystem;

/**
 * This interface defines methods specific to Document objects
 * managed by a Filesystem instance.
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public interface DocumentEntry
    extends Entry
{

    /**
     * get the zize of the document, in bytes
     *
     * @return size in bytes
     */

    public int getSize();

