package org.apache.poi.hpsf;

import java.io.*;
import java.util.*;
import org.apache.poi.hpsf.wellknown.*;

/**
 * <p>Convenience class representing a DocumentSummary Information
 * stream in a Microsoft Office document.</p>
 *
 * @see SummaryInformation
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: DocumentSummaryInformation.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class DocumentSummaryInformation extends SpecialPropertySet
{

    /**
     * <p>Creates a {@link DocumentSummaryInformation} from a given
     * {@link PropertySet}.</p>
     *
     * @param ps A property set which should be created from a
     * document summary information stream.
     *
     * @throws UnexpectedPropertySetTypeException if <var>ps</var>
     * does not contain a document summary information stream.
     */
    public DocumentSummaryInformation(final PropertySet ps)
        throws UnexpectedPropertySetTypeException
    {
        super(ps);
        if (!isDocumentSummaryInformation())
            throw new UnexpectedPropertySetTypeException
                ("Not a " + getClass().getName());
    }



    /**
     * <p>Returns the stream's category (or <code>null</code>).</p>
     */
    public String getCategory()
    {
        return (String) getProperty(PropertyIDMap.PID_CATEGORY);
    }



    /**
     * <p>Returns the stream's presentation format (or
     * <code>null</code>).</p>
     */
    public String getPresentationFormat()
    {
        return (String) getProperty(PropertyIDMap.PID_PRESFORMAT);
    }



    /**
     * <p>Returns the stream's byte count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a byte count.</p>
     */
    public int getByteCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_BYTECOUNT);
    }



    /**
     * <p>Returns the stream's line count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a line count.</p>
     */
    public int getLineCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_LINECOUNT);
    }



    /**
     * <p>Returns the stream's par count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a par count.</p>
     */
    public int getParCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_PARCOUNT);
    }



    /**
     * <p>Returns the stream's slide count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a slide count.</p>
     */
    public int getSlideCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_SLIDECOUNT);
    }



    /**
     * <p>Returns the stream's note count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a note count.</p>
     */
    public int getNoteCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_NOTECOUNT);
    }



    /**
     * <p>Returns the stream's hidden count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a hidden count.</p>
     */
    public int getHiddenCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_HIDDENCOUNT);
    }



    /**
     * <p>Returns the stream's mmclip count or 0 if the {@link
     * DocumentSummaryInformation} does not contain a mmclip count.</p>
     */
    public int getMMClipCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_MMCLIPCOUNT);
    }



    /**
     * <p>Returns the stream's scale (or <code>null</code>)
     * <strong>when this method is implemented. Please note that the
     * return type is likely to change!</strong>
     */
    public byte[] getScale()
    {
        if (true)
            throw new UnsupportedOperationException("FIXME");
        return (byte[]) getProperty(PropertyIDMap.PID_SCALE);
    }



    /**
     * <p>Returns the stream's heading pair (or <code>null</code>)
     * <strong>when this method is implemented. Please note that the
     * return type is likely to change!</strong>
     */
    public byte[] getHeadingPair()
    {
        if (true)
            throw new UnsupportedOperationException("FIXME");
        return (byte[]) getProperty(PropertyIDMap.PID_HEADINGPAIR);
    }



    /**
     * <p>Returns the stream's doc parts (or <code>null</code>)
     * <strong>when this method is implemented. Please note that the
     * return type is likely to change!</strong>
     */
    public byte[] getDocparts()
    {
        if (true)
            throw new UnsupportedOperationException("FIXME");
        return (byte[]) getProperty(PropertyIDMap.PID_DOCPARTS);
    }



    /**
     * <p>Returns the stream's manager (or <code>null</code>).</p>
     */
    public String getManager()
    {
        return (String) getProperty(PropertyIDMap.PID_MANAGER);
    }



    /**
     * <p>Returns the stream's company (or <code>null</code>).</p>
     */
    public String getCompany()
    {
        return (String) getProperty(PropertyIDMap.PID_COMPANY);
    }



    /**
     * <p>Returns the stream's links dirty (or <code>null</code>)
     * <strong>when this method is implemented. Please note that the
     * return type is likely to change!</strong>
     */
    public byte[] getLinksDirty()
    {
        if (true)
            throw new UnsupportedOperationException("FIXME");
        return (byte[]) getProperty(PropertyIDMap.PID_LINKSDIRTY);
    }

}
