package org.apache.tools.ant.taskdefs.cvslib;

/**
 * Represents a RCS File cheange.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:jeff.martin@synamic.co.uk">Jeff Martin</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
class RCSFile {
    private String m_name;
    private String m_revision;
    private String m_previousRevision;


    RCSFile(final String name, final String rev) {
        this(name, rev, null);
    }


    RCSFile(final String name,
                  final String revision,
                  final String previousRevision) {
        m_name = name;
        m_revision = revision;
        if (!revision.equals(previousRevision)) {
            m_previousRevision = previousRevision;
        }
    }


    String getName() {
        return m_name;
    }


    String getRevision() {
        return m_revision;
    }


    String getPreviousRevision() {
        return m_previousRevision;
    }
}

