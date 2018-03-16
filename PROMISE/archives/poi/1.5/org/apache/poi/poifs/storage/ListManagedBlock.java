package org.apache.poi.poifs.storage;

import java.io.IOException;

/**
 * An interface for blocks managed by a list that works with a
 * BlockAllocationTable to keep block sequences straight
 *
 * @author Marc Johnson (mjohnson at apache dot org
 */

public interface ListManagedBlock
{

    /**
     * Get the data from the block
     *
     * @return the block's data as a byte array
     *
     * @exception IOException if there is no data
     */

    public byte [] getData()
        throws IOException;

