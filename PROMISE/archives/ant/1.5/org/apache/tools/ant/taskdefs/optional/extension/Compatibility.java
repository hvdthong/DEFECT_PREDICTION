package org.apache.tools.ant.taskdefs.optional.extension;

/**
 * Enum used in (@link Extension) to indicate the compatibility
 * of one extension to another. See (@link Extension) for instances
 * of object.
 *
 * WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
 *  This file is from excalibur.extension package. Dont edit this file
 * directly as there is no unit tests to make sure it is operational
 * in ant. Edit file in excalibur and run tests there before changing
 * ants file.
 * WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 * @see Extension
 */
public final class Compatibility
{
    /**
     * A string representaiton of compatibility level.
     */
    private final String m_name;

    /**
     * Create a compatibility enum with specified name.
     *
     * @param name the name of compatibility level
     */
    Compatibility( final String name )
    {
        m_name = name;
    }

    /**
     * Return name of compatibility level.
     *
     * @return the name of compatibility level
     */
    public String toString()
    {
        return m_name;
    }
}
