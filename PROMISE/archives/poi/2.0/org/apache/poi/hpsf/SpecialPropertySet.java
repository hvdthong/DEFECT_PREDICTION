package org.apache.poi.hpsf;

import java.util.*;
import org.apache.poi.util.LittleEndian;

/**
 * <p>Abstract superclass for the convenience classes {@link
 * SummaryInformation} and {@link DocumentSummaryInformation}.</p>
 *
 * <p>The motivation behind this class is quite nasty if you look
 * behind the scenes, but it serves the application programmer well by
 * providing him with the easy-to-use {@link SummaryInformation} and
 * {@link DocumentSummaryInformation} classes. When parsing the data a
 * property set stream consists of (possibly coming from an {@link
 * java.io.InputStream}) we want to read and process each byte only
 * once. Since we don't know in advance which kind of property set we
 * have, we can expect only the most general {@link
 * PropertySet}. Creating a special subclass should be as easy as
 * calling the special subclass' constructor and pass the general
 * {@link PropertySet} in. To make things easy internally, the special
 * class just holds a reference to the general {@link PropertySet} and
 * delegates all method calls to it.</p>
 *
 * <p>A cleaner implementation would have been like this: The {@link
 * PropertySetFactory} parses the stream data into some internal
 * object first.  Then it finds out whether the stream is a {@link
 * SummaryInformation}, a {@link DocumentSummaryInformation} or a
 * general {@link PropertySet}.  However, the current implementation
 * went the other way round historically: the convenience classes came
 * only late to my mind.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: SpecialPropertySet.java 353013 2003-02-22 14:27:16Z klute $
 * @since 2002-02-09
 */
public abstract class SpecialPropertySet extends PropertySet
{

    private PropertySet delegate;



    public SpecialPropertySet(PropertySet ps)
    {
        delegate = ps;
    }



    public int getByteOrder()
    {
        return delegate.getByteOrder();
    }



    public int getFormat()
    {
        return delegate.getFormat();
    }



    /*
     * This is intentionally no javadoc comment.
     *
     * FIXME: Return an <code>int</code> instead of a
     * <code>long</code> in the next major version, i.e. when
     * incompatible changes are allowed.
     */
    public long getOSVersion()
    {
        return delegate.getOSVersion();
    }



    public ClassID getClassID()
    {
        return delegate.getClassID();
    }



    /*
     * This is intentionally no javadoc comment.
     *
     * FIXME: Return an <code>int</code> instead of a
     * <code>long</code> in the next major version, i.e. when
     * incompatible changes are allowed.
     */
    public long getSectionCount()
    {
        return delegate.getSectionCount();
    }



    public List getSections()
    {
        return delegate.getSections();
    }



    public boolean isSummaryInformation()
    {
        return delegate.isSummaryInformation();
    }



    public boolean isDocumentSummaryInformation()
    {
        return delegate.isDocumentSummaryInformation();
    }



    public Section getSingleSection()
    {
        return delegate.getSingleSection();
    }

}
