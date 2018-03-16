package org.apache.poi.poifs.storage;

import java.util.*;

/**
 * A list of SmallDocumentBlocks instances, and methods to manage the list
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class SmallDocumentBlockList
    extends BlockListImpl
{

    /**
     * Constructor SmallDocumentBlockList
     *
     * @param blocks a list of SmallDocumentBlock instances
     */

    public SmallDocumentBlockList(final List blocks)
    {
        setBlocks(( SmallDocumentBlock [] ) blocks
            .toArray(new SmallDocumentBlock[ 0 ]));
    }

