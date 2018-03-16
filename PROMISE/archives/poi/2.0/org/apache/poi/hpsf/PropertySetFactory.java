package org.apache.poi.hpsf;

import java.io.*;

/**
 * <p>Factory class to create instances of {@link SummaryInformation},
 * {@link DocumentSummaryInformation} and {@link PropertySet}.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: PropertySetFactory.java 352995 2003-02-01 13:28:28Z klute $
 * @since 2002-02-09
 */
public class PropertySetFactory
{

    /**
     * <p>Creates the most specific {@link PropertySet} from an {@link
     * InputStream}. This is preferrably a {@link
     * DocumentSummaryInformation} or a {@link SummaryInformation}. If
     * the specified {@link InputStream} does not contain a property
     * set stream, an exception is thrown and the {@link InputStream}
     * is repositioned at its beginning.</p>
     *
     * @param stream Contains the property set stream's data.
     * @return The created {@link PropertySet}.
     * @throws NoPropertySetStreamException if the stream does not
     * contain a property set.
     * @throws MarkUnsupportedException if the stream does not support
     * the <code>mark</code> operation.
     * @throws UnexpectedPropertySetTypeException if the property
     * set's type is unexpected.
     * @throws IOException if some I/O problem occurs.
     */
    public static PropertySet create(final InputStream stream)
	throws NoPropertySetStreamException, MarkUnsupportedException,
	       UnexpectedPropertySetTypeException, IOException
    {
        final PropertySet ps = new PropertySet(stream);
        if (ps.isSummaryInformation())
            return new SummaryInformation(ps);
        else if (ps.isDocumentSummaryInformation())
            return new DocumentSummaryInformation(ps);
        else
            return ps;
    }

}
