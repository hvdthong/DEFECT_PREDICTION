package org.apache.poi.hpsf;

import java.io.*;
import java.util.*;
import org.apache.poi.hpsf.wellknown.*;

/**
 * <p>Convenience class representing a Summary Information stream in a
 * Microsoft Office document.</p>
 *
 * @see DocumentSummaryInformation
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: SummaryInformation.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class SummaryInformation extends SpecialPropertySet
{

    /**
     * <p>Creates a {@link SummaryInformation} from a given {@link
     * PropertySet}.</p>
     *
     * @param ps A property set which should be created from a summary
     * information stream.
     *
     * @throws UnexpectedPropertySetTypeException if <var>ps</var>
     * does not contain a summary information stream.
     */
    public SummaryInformation(final PropertySet ps)
        throws UnexpectedPropertySetTypeException
    {
        super(ps);
        if (!isSummaryInformation())
            throw new UnexpectedPropertySetTypeException
                ("Not a " + getClass().getName());
    }



    /**
     * <p>Returns the stream's title (or <code>null</code>).</p>
     */
    public String getTitle()
    {
        return (String) getProperty(PropertyIDMap.PID_TITLE);
    }



    /**
     * <p>Returns the stream's subject (or <code>null</code>).</p>
     */
    public String getSubject()
    {
        return (String) getProperty(PropertyIDMap.PID_SUBJECT);
    }



    /**
     * <p>Returns the stream's author (or <code>null</code>).</p>
     */
    public String getAuthor()
    {
        return (String) getProperty(PropertyIDMap.PID_AUTHOR);
    }



    /**
     * <p>Returns the stream's keywords (or <code>null</code>).</p>
     */
    public String getKeywords()
    {
        return (String) getProperty(PropertyIDMap.PID_KEYWORDS);
    }



    /**
     * <p>Returns the stream's comments (or <code>null</code>).</p>
     */
    public String getComments()
    {
        return (String) getProperty(PropertyIDMap.PID_COMMENTS);
    }



    /**
     * <p>Returns the stream's template (or <code>null</code>).</p>
     */
    public String getTemplate()
    {
        return (String) getProperty(PropertyIDMap.PID_TEMPLATE);
    }



    /**
     * <p>Returns the stream's last author (or <code>null</code>).</p>
     */
    public String getLastAuthor()
    {
        return (String) getProperty(PropertyIDMap.PID_LASTAUTHOR);
    }



    /**
     * <p>Returns the stream's revision number (or
     * <code>null</code>).
     </p> */
    public String getRevNumber()
    {
        return (String) getProperty(PropertyIDMap.PID_REVNUMBER);
    }



    /**
     * <p>Returns the stream's edit time (or <code>null</code>).</p>
     */
    public Date getEditTime()
    {
        return (Date) getProperty(PropertyIDMap.PID_EDITTIME);
    }



    /**
     * <p>Returns the stream's last printed time (or
     * <code>null</code>).</p>
     */
    public Date getLastPrinted()
    {
        return (Date) getProperty(PropertyIDMap.PID_LASTPRINTED);
    }



    /**
     * <p>Returns the stream's creation time (or
     * <code>null</code>).</p>
     */
    public Date getCreateDateTime()
    {
        return (Date) getProperty(PropertyIDMap.PID_CREATE_DTM);
    }



    /**
     * <p>Returns the stream's last save time (or
     * <code>null</code>).</p>
     */
    public Date getLastSaveDateTime()
    {
        return (Date) getProperty(PropertyIDMap.PID_LASTSAVE_DTM);
    }



    /**
     * <p>Returns the stream's page count or 0 if the {@link
     * SummaryInformation} does not contain a page count.</p>
     */
    public int getPageCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_PAGECOUNT);
    }



    /**
     * <p>Returns the stream's word count or 0 if the {@link
     * SummaryInformation} does not contain a word count.</p>
     */
    public int getWordCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_WORDCOUNT);
    }



    /**
     * <p>Returns the stream's char count or 0 if the {@link
     * SummaryInformation} does not contain a char count.</p>
     */
    public int getCharCount()
    {
        return getPropertyIntValue(PropertyIDMap.PID_CHARCOUNT);
    }



    /**
     * <p>Returns the stream's thumbnail (or <code>null</code>)
     * <strong>when this method is implemented. Please note that the
     * return type is likely to change!</strong>
     */
    public byte[] getThumbnail()
    {
        if (true)
            throw new UnsupportedOperationException("FIXME");
        return (byte[]) getProperty(PropertyIDMap.PID_THUMBNAIL);
    }



    /**
     * <p>Returns the stream's application name (or
     * <code>null</code>).</p>
     */
    public String getApplicationName()
    {
        return (String) getProperty(PropertyIDMap.PID_APPNAME);
    }



    /**
     * <p>Returns the stream's security field or 0 if the {@link
     * SummaryInformation} does not contain a security field.</p>
     */
    public int getSecurity()
    {
        return getPropertyIntValue(PropertyIDMap.PID_SECURITY);
    }

}
