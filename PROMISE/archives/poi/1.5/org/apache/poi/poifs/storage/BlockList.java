package org.apache.poi.poifs.storage;

import java.io.IOException;

/**
 * Interface for lists of blocks that are mapped by block allocation
 * tables
 *
 * @author Marc Johnson (mjohnson at apache dot org
 */

public interface BlockList
{

    /**
     * remove the specified block from the list
     *
     * @param index the index of the specified block; if the index is
     *              out of range, that's ok
     */

    public void zap(final int index);

    /**
     * remove and return the specified block from the list
     *
     * @param index the index of the specified block
     *
     * @return the specified block
     *
     * @exception IOException if the index is out of range or has
     *            already been removed
     */

    public ListManagedBlock remove(final int index)
        throws IOException;

    /**
     * get the blocks making up a particular stream in the list. The
     * blocks are removed from the list.
     *
     * @param startBlock the index of the first block in the stream
     *
     * @return the stream as an array of correctly ordered blocks
     *
     * @exception IOException if blocks are missing
     */

    public ListManagedBlock [] fetchBlocks(final int startBlock)
        throws IOException;

    /**
     * set the associated BlockAllocationTable
     *
     * @param bat the associated BlockAllocationTable
     *
     * @exception IOException
     */

    public void setBAT(final BlockAllocationTableReader bat)
        throws IOException;

