package org.apache.poi.poifs.storage;

import org.apache.poi.poifs.property.RootProperty;

import java.util.*;

import java.io.*;

/**
 * This class implements reading the small document block list from an
 * existing file
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class SmallBlockTableReader
{

    /**
     * fetch the small document block list from an existing file
     *
     * @param blockList the raw data from which the small block table
     *                  will be extracted
     * @param root the root property (which contains the start block
     *             and small block table size)
     * @param sbatStart the start block of the SBAT
     *
     * @return the small document block list
     *
     * @exception IOException
     */

    public static BlockList getSmallDocumentBlocks(
            final RawDataBlockList blockList, final RootProperty root,
            final int sbatStart)
        throws IOException
    {
        BlockList list =
            new SmallDocumentBlockList(SmallDocumentBlock
                .extract(blockList.fetchBlocks(root.getStartBlock())));

        new BlockAllocationTableReader(blockList.fetchBlocks(sbatStart),
                                       list);
        return list;
    }
}
