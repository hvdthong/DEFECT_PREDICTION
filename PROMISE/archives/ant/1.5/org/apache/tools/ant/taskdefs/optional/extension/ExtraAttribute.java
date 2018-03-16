package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.BuildException;

/**
 * Simple holder for extra attributes in main section of manifest.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 * @todo Refactor this and all the other parameter, sysproperty,
 *   property etc into a single class in framework
 */
public class ExtraAttribute
{
    private String m_name;
    private String m_value;

    /**
     * Set the name of the parameter.
     *
     * @param name the name of parameter
     */
    public void setName( final String name )
    {
        m_name = name;
    }

    /**
     * Set the value of the parameter.
     *
     * @param value the parameter value
     */
    public void setValue( final String value )
    {
        m_value = value;
    }

    /**
     * Retrieve name of parameter.
     *
     * @return the name of parameter.
     */
    String getName()
    {
        return m_name;
    }

    /**
     * Retrieve the value of parameter.
     *
     * @return the value of parameter.
     */
    String getValue()
    {
        return m_value;
    }

    /**
     * Make sure that neither the name or the value
     * is null.
     */
    public void validate()
        throws BuildException
    {
        if( null == m_name )
        {
            final String message = "Missing name from parameter.";
            throw new BuildException( message );
        }
        else if( null == m_value )
        {
            final String message = "Missing value from parameter " + m_name + ".";
            throw new BuildException( message );
        }
    }
}
