package org.apache.poi.hpsf;

/**
 * <p>This exception is thrown when there is an illegal value set in a
 * {@link PropertySet}. For example, a {@link Variant#VT_BOOL} must
 * have a value of <code>-1 (true)</code> or <code>0 (false)</code>.
 * Any other value would trigger this exception. It supports a nested
 * "reason" throwable, i.e. an exception that caused this one to be
 * thrown.</p>
 *
 * @author Drew Varner(Drew.Varner atDomain sc.edu)
 * @version $Id: IllegalPropertySetDataException.java 352995 2003-02-01 13:28:28Z klute $
 * @since 2002-05-26
 */
public class  IllegalPropertySetDataException extends HPSFRuntimeException
{

    public IllegalPropertySetDataException()
    {
        super();
    }



    public IllegalPropertySetDataException(final String msg)
    {
        super(msg);
    }



    public IllegalPropertySetDataException(final Throwable reason)
    {
        super(reason);
    }



    public IllegalPropertySetDataException(final String msg,
                                           final Throwable reason)
    {
        super(msg,reason);
    }

}
