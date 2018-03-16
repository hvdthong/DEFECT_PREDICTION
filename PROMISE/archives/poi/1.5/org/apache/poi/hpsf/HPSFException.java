package org.apache.poi.hpsf;

/**
 * <p>This exception is the superclass of all other checked exceptions
 * thrown in this package. It supports a nested "reason" throwable,
 * i.e. an exception that caused this one to be thrown.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: HPSFException.java 352095 2002-02-14 04:00:59Z mjohnson $
 * @since 2002-02-09
 */
public class HPSFException extends Exception
{

    private Throwable reason;



    /**
     * <p>Creates a new {@link HPSFException}.</p>
     */
    public HPSFException()
    {
        super();
    }



    /**
     * <p>Creates a new {@link HPSFException} with a message
     * string.</p>
     */
    public HPSFException(final String msg)
    {
        super(msg);
    }



    /**
     * <p>Creates a new {@link HPSFException} with a reason.</p>
     */
    public HPSFException(final Throwable reason)
    {
        super();
        this.reason = reason;
    }



    /**
     * <p>Creates a new {@link HPSFException} with a message string
     * and a reason.</p>
     */
    public HPSFException(final String msg, final Throwable reason)
    {
        super(msg);
        this.reason = reason;
    }



    /**
     * <p>Returns the {@link Throwable} that caused this exception to
     * be thrown or <code>null</code> if there was no such {@link
     * Throwable}.</p>
     */
    public Throwable getReason()
    {
        return reason;
    }

}
