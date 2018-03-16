package org.apache.poi.poifs.property;

import org.apache.poi.poifs.filesystem.POIFSDocument;

/**
 * Trivial extension of Property for POIFSDocuments
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */

public class DocumentProperty
    extends Property
{

    private POIFSDocument _document;

    /**
     * Constructor
     *
     * @param name POIFSDocument name
     * @param size POIFSDocument size
     */

    public DocumentProperty(final String name, final int size)
    {
        super();
        _document = null;
        setName(name);
        setSize(size);
        setPropertyType(PropertyConstants.DOCUMENT_TYPE);
    }

    /**
     * reader constructor
     *
     * @param index index number
     * @param array byte data
     * @param offset offset into byte data
     */

    protected DocumentProperty(final int index, final byte [] array,
                               final int offset)
    {
        super(index, array, offset);
        _document = null;
    }

    /**
     * set the POIFSDocument
     *
     * @param doc the associated POIFSDocument
     */

    public void setDocument(POIFSDocument doc)
    {
        _document = doc;
    }

    /**
     * get the POIFSDocument
     *
     * @return the associated document
     */

    public POIFSDocument getDocument()
    {
        return _document;
    }

    /* ********** START extension of Property ********** */

    /**
     * give method more visibility
     *
     * @return true if this property should use small blocks
     */

    public boolean shouldUseSmallBlocks()
    {
        return super.shouldUseSmallBlocks();
    }

    /**
     * @return true if a directory type Property
     */

    public boolean isDirectory()
    {
        return false;
    }

    /**
     * Perform whatever activities need to be performed prior to
     * writing
     */

    protected void preWrite()
    {

    }

    /* **********  END  extension of Property ********** */

