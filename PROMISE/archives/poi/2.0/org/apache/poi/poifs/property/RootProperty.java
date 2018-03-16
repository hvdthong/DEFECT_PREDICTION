package org.apache.poi.poifs.property;

import java.util.*;

import java.io.IOException;

import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.storage.SmallDocumentBlock;

/**
 * Root property
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class RootProperty
    extends DirectoryProperty
{

    /**
     * Default constructor
     */

    RootProperty()
    {
        super("Root Entry");

        setNodeColor(_NODE_BLACK);
        setPropertyType(PropertyConstants.ROOT_TYPE);
        setStartBlock(POIFSConstants.END_OF_CHAIN);
    }

    /**
     * reader constructor
     *
     * @param index index number
     * @param array byte data
     * @param offset offset into byte data
     */

    protected RootProperty(final int index, final byte [] array,
                           final int offset)
    {
        super(index, array, offset);
    }

    /**
     * set size
     *
     * @param size size in terms of small blocks
     */

    public void setSize(int size)
    {
        super.setSize(SmallDocumentBlock.calcSize(size));
    }

