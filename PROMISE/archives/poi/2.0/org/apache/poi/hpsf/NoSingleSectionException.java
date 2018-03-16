package org.apache.poi.hpsf;

/**
 * <p>This exception is thrown if one of the {@link PropertySet}'s
 * convenience methods that require a single {@link Section} is called
 * and the {@link PropertySet} does not contain exactly one {@link
 * Section}.</p>
 *
 * <p>The constructors of this class are analogous to those of its
 * superclass and documented there.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: NoSingleSectionException.java 352995 2003-02-01 13:28:28Z klute $
 * @since 2002-02-09
 */
public class NoSingleSectionException extends HPSFRuntimeException
{

    public NoSingleSectionException()
    {
        super();
    }


    public NoSingleSectionException(final String msg)
    {
        super(msg);
    }


    public NoSingleSectionException(final Throwable reason)
    {
        super(reason);
    }


    public NoSingleSectionException(final String msg, final Throwable reason)
    {
        super(msg, reason);
    }

}
