package org.apache.tools.ant.util;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;

/**
 * A facade that makes logging nicers to use.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
public final class TaskLogger
{
    /**
     * Task to use to do logging.
     */
    private Task m_task;

    public TaskLogger( final Task task )
    {
        this.m_task = task;
    }

    public void info( final String message )
    {
        m_task.log( message, Project.MSG_INFO );
    }

    public void error( final String message )
    {
        m_task.log( message, Project.MSG_ERR );
    }

    public void warning( final String message )
    {
        m_task.log( message, Project.MSG_WARN );
    }

    public void verbose( final String message )
    {
        m_task.log( message, Project.MSG_VERBOSE );
    }

    public void debug( final String message )
    {
        m_task.log( message, Project.MSG_DEBUG );
    }
}
