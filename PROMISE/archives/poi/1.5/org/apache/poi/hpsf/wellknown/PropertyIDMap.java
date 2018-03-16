package org.apache.poi.hpsf.wellknown;

import java.util.*;

/**
 * <p>This is a dictionary mapping property IDs to property ID
 * strings.</p>
 *
 * <p>The methods {@link #getSummaryInformationProperties} and {@link
 * #getDocumentSummaryInformationProperties} return singleton {@link
 * PropertyIDMap}s. An application that wants to extend these maps
 * should treat them as unmodifiable, copy them and modifiy the
 * copies.</p>
 *
 * <p><strong>FIXME:</strong> Make the singletons
 * unmodifiable. However, since this requires use a {@link HashMap}
 * delegate instead of extending {@link HashMap} and would require a
 * lot of stupid typing, I won't do it for the time being.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: PropertyIDMap.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class PropertyIDMap extends HashMap
{

    /* The following definitions are for the Summary Information. */
    public static final int PID_TITLE = 2;
    public static final int PID_SUBJECT = 3;
    public static final int PID_AUTHOR = 4;
    public static final int PID_KEYWORDS = 5;
    public static final int PID_COMMENTS = 6;
    public static final int PID_TEMPLATE = 7;
    public static final int PID_LASTAUTHOR = 8;
    public static final int PID_REVNUMBER = 9;
    public static final int PID_EDITTIME = 10;
    public static final int PID_LASTPRINTED = 11;
    public static final int PID_CREATE_DTM = 12;
    public static final int PID_LASTSAVE_DTM = 13;
    public static final int PID_PAGECOUNT = 14;
    public static final int PID_WORDCOUNT = 15;
    public static final int PID_CHARCOUNT = 16;
    public static final int PID_THUMBNAIL = 17;
    public static final int PID_APPNAME = 18;
    public static final int PID_SECURITY = 19;

    /* The following definitions are for the Document Summary Information. */
    public static final int PID_CATEGORY = 2;
    public static final int PID_PRESFORMAT = 3;
    public static final int PID_BYTECOUNT = 4;
    public static final int PID_LINECOUNT = 5;
    public static final int PID_PARCOUNT = 6;
    public static final int PID_SLIDECOUNT = 7;
    public static final int PID_NOTECOUNT = 8;
    public static final int PID_HIDDENCOUNT = 9;
    public static final int PID_MMCLIPCOUNT = 10;
    public static final int PID_SCALE = 11;
    public static final int PID_HEADINGPAIR = 12;
    public static final int PID_DOCPARTS = 13;
    public static final int PID_MANAGER = 14;
    public static final int PID_COMPANY = 15;
    public static final int PID_LINKSDIRTY = 16;

    private static PropertyIDMap summaryInformationProperties;
    private static PropertyIDMap documentSummaryInformationProperties;



    public PropertyIDMap(int initialCapacity, float loadFactor)
    {
	super(initialCapacity, loadFactor);
    }



    /**
     * <p>Puts a ID string for an ID into the {@link
     * PropertyIDMap}.</p>
     *
     * @param id The ID.
     *
     * @param idString The ID string.
     */
    public Object put(int id, String idString)
    {
	return put(new Integer(id), idString);
    }



    /**
     * <p>Gets the ID string for an ID from the {@link
     * PropertyIDMap}.</p>
     *
     * @param id The ID.
     */
    public Object get(int id)
    {
	return get(new Integer(id));
    }



    /**
     * <p>Returns the Summary Information properties singleton.</p>
     */
    public static PropertyIDMap getSummaryInformationProperties()
    {
	if (summaryInformationProperties == null)
	{
	    PropertyIDMap m = new PropertyIDMap(17, (float) 1.0);
	    m.put(PID_TITLE, "PID_TITLE");
	    m.put(PID_SUBJECT, "PID_SUBJECT");
	    m.put(PID_AUTHOR, "PID_AUTHOR");
	    m.put(PID_KEYWORDS, "PID_KEYWORDS");
	    m.put(PID_COMMENTS, "PID_COMMENTS");
	    m.put(PID_TEMPLATE, "PID_TEMPLATE");
	    m.put(PID_LASTAUTHOR, "PID_LASTAUTHOR");
	    m.put(PID_REVNUMBER, "PID_REVNUMBER");
	    m.put(PID_EDITTIME, "PID_EDITTIME");
	    m.put(PID_LASTPRINTED, "PID_LASTPRINTED");
	    m.put(PID_CREATE_DTM, "PID_CREATE_DTM");
	    m.put(PID_LASTSAVE_DTM, "PID_LASTSAVE_DTM");
	    m.put(PID_PAGECOUNT, "PID_PAGECOUNT");
	    m.put(PID_WORDCOUNT, "PID_WORDCOUNT");
	    m.put(PID_CHARCOUNT, "PID_CHARCOUNT");
	    m.put(PID_THUMBNAIL, "PID_THUMBNAIL");
	    m.put(PID_APPNAME, "PID_APPNAME");
	    m.put(PID_SECURITY, "PID_SECURITY");
	    summaryInformationProperties = m;
	}
	return summaryInformationProperties;
    }



    /**
     * <p>Returns the Summary Information properties singleton.</p>
     */
    public static PropertyIDMap getDocumentSummaryInformationProperties()
    {
	if (documentSummaryInformationProperties == null)
	{
	    PropertyIDMap m = new PropertyIDMap(17, (float) 1.0);
	    m.put(PID_CATEGORY, "PID_CATEGORY");
	    m.put(PID_PRESFORMAT, "PID_PRESFORMAT");
	    m.put(PID_BYTECOUNT, "PID_BYTECOUNT");
	    m.put(PID_LINECOUNT, "PID_LINECOUNT");
	    m.put(PID_PARCOUNT, "PID_PARCOUNT");
	    m.put(PID_SLIDECOUNT, "PID_SLIDECOUNT");
	    m.put(PID_NOTECOUNT, "PID_NOTECOUNT");
	    m.put(PID_HIDDENCOUNT, "PID_HIDDENCOUNT");
	    m.put(PID_MMCLIPCOUNT, "PID_MMCLIPCOUNT");
	    m.put(PID_SCALE, "PID_SCALE");
	    m.put(PID_HEADINGPAIR, "PID_HEADINGPAIR");
	    m.put(PID_DOCPARTS, "PID_DOCPARTS");
	    m.put(PID_MANAGER, "PID_MANAGER");
	    m.put(PID_COMPANY, "PID_COMPANY");
	    m.put(PID_LINKSDIRTY, "PID_LINKSDIRTY");
	    documentSummaryInformationProperties = m;
	}
	return documentSummaryInformationProperties;

    }



    public static void main(String args[])
    {
	PropertyIDMap s1 = getSummaryInformationProperties();
	PropertyIDMap s2 = getDocumentSummaryInformationProperties();
	System.out.println("s1: " + s1);
	System.out.println("s2: " + s2);
    }

}
