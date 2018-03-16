package org.apache.poi.poifs.filesystem;

/**
 * This interface defines behaviors for objects managed by the Block
 * Allocation Table (BAT).
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public interface BATManaged
{

    /**
     * Return the number of BigBlock's this instance uses
     *
     * @return count of BigBlock instances
     */

    public int countBlocks();

    /**
     * Set the start block for this instance
     *
     * @param index index into the array of BigBlock instances making
     *              up the the filesystem
     */

    public void setStartBlock(final int index);

