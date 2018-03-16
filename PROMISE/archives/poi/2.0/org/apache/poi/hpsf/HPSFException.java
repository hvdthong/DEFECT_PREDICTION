package org.apache.poi.hpsf;

/**
 * <p>This exception is the superclass of all other checked exceptions
 * thrown in this package. It supports a nested "reason" throwable,
 * i.e. an exception that caused this one to be thrown.</p>
 *
 * @author Rainer Klute (klute@rainer-klute.de)
 * @version $Id: HPSFException.java 352995 2003-02-01 13:28:28Z klute $
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
     *
     * @param msg The message string.
     */
    public HPSFException(final String msg)
    {
        super(msg);
    }



    /**
     * <p>Creates a new {@link HPSFException} with a reason.</p>
     *
     * @param reason The reason, i.e. a throwable that indirectly
     * caused this exception.
     */
    public HPSFException(final Throwable reason)
    {
        super();
        this.reason = reason;
    }



    /**
     * <p>Creates a new {@link HPSFException} with a message string
     * and a reason.</p>
     *
     * @param msg The message string.
     * @param reason The reason, i.e. a throwable that indirectly
     * caused this exception.
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
     *
     * @return The reason
     */
    public Throwable getReason()
    {
        return reason;
    }

}
