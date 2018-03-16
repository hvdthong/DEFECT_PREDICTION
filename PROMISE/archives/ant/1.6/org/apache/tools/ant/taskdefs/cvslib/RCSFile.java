package org.apache.tools.ant.taskdefs.cvslib;

/**
 * Represents a RCS File change.
 *
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

