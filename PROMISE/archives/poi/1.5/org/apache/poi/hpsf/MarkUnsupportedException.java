package org.apache.poi.hpsf;

/**
 * <p>This exception is thrown if an {@link java.io.InputStream} does
 * not support the {@link java.io.InputStream#mark} operation.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: MarkUnsupportedException.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class MarkUnsupportedException extends HPSFException
{

    public MarkUnsupportedException()
    {
        super();
    }

    public MarkUnsupportedException(final String msg)
    {
        super(msg);
    }

    public MarkUnsupportedException(final Throwable reason)
    {
        super(reason);
    }

    public MarkUnsupportedException(final String msg, final Throwable reason)
    {
        super(msg, reason);
    }

}
