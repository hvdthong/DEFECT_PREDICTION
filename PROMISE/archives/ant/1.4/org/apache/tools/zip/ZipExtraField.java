package org.apache.tools.zip;

import java.util.zip.ZipException;

/**
 * General format of extra field data.
 *
 * <p>Extra fields usually appear twice per file, once in the local
 * file data and once in the central directory.  Usually they are the
 * same, but they don't have to be.  {@link
 * java.util.zip.ZipOutputStream java.util.zip.ZipOutputStream} will
 * only use the local file data in both places.</p>
 *
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 269271 $
 */
public interface ZipExtraField {

    /**
     * The Header-ID.
     *
     * @since 1.1
     */
    public ZipShort getHeaderId();

    /**
     * Length of the extra field in the local file data - without
     * Header-ID or length specifier.
     *
     * @since 1.1
     */
    public ZipShort getLocalFileDataLength();

    /**
     * Length of the extra field in the central directory - without
     * Header-ID or length specifier.
     *
     * @since 1.1
     */
    public ZipShort getCentralDirectoryLength();

    /**
     * The actual data to put into local file data - without Header-ID
     * or length specifier.
     *
     * @since 1.1
     */
    public byte[] getLocalFileDataData();

    /**
     * The actual data to put central directory - without Header-ID or
     * length specifier.
     *
     * @since 1.1
     */
    public byte[] getCentralDirectoryData();

    /**
     * Populate data from this array as if it was in local file data.
     *
     * @since 1.1
     */
    public void parseFromLocalFileData(byte[] data, int offset, int length)
        throws ZipException;
}
