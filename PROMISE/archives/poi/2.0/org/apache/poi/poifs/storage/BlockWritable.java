package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An interface for persisting block storage of POIFS components.
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public interface BlockWritable
{

    /**
     * Write the storage to an OutputStream
     *
     * @param stream the OutputStream to which the stored data should
     *               be written
     *
     * @exception IOException on problems writing to the specified
     *            stream
     */

    public void writeBlocks(final OutputStream stream)
        throws IOException;

