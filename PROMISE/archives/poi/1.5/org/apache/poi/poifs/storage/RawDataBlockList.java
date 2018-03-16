package org.apache.poi.poifs.storage;

import java.io.*;

import java.util.*;

/**
 * A list of RawDataBlocks instances, and methods to manage the list
 *
 * @author Marc Johnson (mjohnson at apache dot org
 */

public class RawDataBlockList
    extends BlockListImpl
{

    /**
     * Constructor RawDataBlockList
     *
     * @param stream the InputStream from which the data will be read
     *
     * @exception IOException on I/O errors, and if an incomplete
     *            block is read
     */

    public RawDataBlockList(final InputStream stream)
        throws IOException
    {
        List blocks = new ArrayList();

        while (true)
        {
            RawDataBlock block = new RawDataBlock(stream);

            if (block.eof())
            {
                break;
            }
            blocks.add(block);
        }
        setBlocks(( RawDataBlock [] ) blocks.toArray(new RawDataBlock[ 0 ]));
    }

