package org.apache.poi.poifs.filesystem;

/**
 * Class POIFSWriterEvent
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 * @version %I%, %G%
 */

public class POIFSWriterEvent
{
    private DocumentOutputStream stream;
    private POIFSDocumentPath    path;
    private String               documentName;
    private int                  limit;

    /**
     * package scoped constructor
     *
     * @param stream the DocumentOutputStream, freshly opened
     * @param path the path of the document
     * @param documentName the name of the document
     * @param limit the limit, in bytes, that can be written to the
     *              stream
     */

    POIFSWriterEvent(final DocumentOutputStream stream,
                     final POIFSDocumentPath path, final String documentName,
                     final int limit)
    {
        this.stream       = stream;
        this.path         = path;
        this.documentName = documentName;
        this.limit        = limit;
    }

    /**
     * @return the DocumentOutputStream, freshly opened
     */

    public DocumentOutputStream getStream()
    {
        return stream;
    }

    /**
     * @return the document's path
     */

    public POIFSDocumentPath getPath()
    {
        return path;
    }

    /**
     * @return the document's name
     */

    public String getName()
    {
        return documentName;
    }

    /**
     * @return the limit on writing, in bytes
     */

    public int getLimit()
    {
        return limit;
    }

