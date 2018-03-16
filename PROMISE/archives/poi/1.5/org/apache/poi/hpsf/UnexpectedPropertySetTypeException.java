package org.apache.poi.hpsf;

/**
 * <p>This exception is thrown if a certain type of property set is
 * expected (e.g. a Document Summary Information) but the provided
 * property set is not of that type.</p>
 *
 * <p>The constructors of this class are analogous to those of its
 * superclass and documented there.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: UnexpectedPropertySetTypeException.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class UnexpectedPropertySetTypeException extends HPSFException
{

    public UnexpectedPropertySetTypeException()
    {
        super();
    }

    public UnexpectedPropertySetTypeException(final String msg)
    {
        super(msg);
    }

    public UnexpectedPropertySetTypeException(final Throwable reason)
    {
        super(reason);
    }

    public UnexpectedPropertySetTypeException(final String msg,
                                              final Throwable reason)
    {
        super(msg, reason);
    }

}
