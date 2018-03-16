package org.apache.poi.hpsf;

/**
 *  <p>
 *
 *  This exception is thrown if a format error in a property set stream is
 *  detected or when the input data do not constitute a property set stream.</p>
 *  <p>
 *
 *  The constructors of this class are analogous to those of its superclass and
 *  documented there.</p>
 *
 *@author     Rainer Klute (klute@rainer-klute.de)
 *@version    $Id: NoPropertySetStreamException.java 352995 2003-02-01 13:28:28Z klute $
 *@since      2002-02-09
 */
public class NoPropertySetStreamException extends HPSFException
{

    public NoPropertySetStreamException()
    {
        super();
    }


    public NoPropertySetStreamException(final String msg)
    {
        super(msg);
    }


    public NoPropertySetStreamException(final Throwable reason)
    {
        super(reason);
    }


    public NoPropertySetStreamException(final String msg,
					final Throwable reason)
    {
        super(msg, reason);
    }

}
