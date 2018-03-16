package org.apache.tools.ant.taskdefs.cvslib;

/**
 * Holds the information of a line of rdiff
 */
class CvsTagEntry {
    String m_filename;
    String m_prevRevision;
    String m_revision;

    public CvsTagEntry(String filename) {
        this(filename, null, null);
    }

    public CvsTagEntry(String filename, String revision) {
        this(filename, revision, null);
    }

    public CvsTagEntry(String filename, String revision,
                       String prevRevision) {
        m_filename = filename;
        m_revision = revision;
        m_prevRevision = prevRevision;
    }

    public String getFile() {
        return m_filename;
    }

    public String getRevision() {
        return m_revision;
    }

    public String getPreviousRevision() {
        return m_prevRevision;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(m_filename);
        if ((m_revision == null) && (m_prevRevision == null)) {
            buffer.append(" was removed");
        } else if (m_revision != null && m_prevRevision == null) {
            buffer.append(" is new; current revision is ")
                .append(m_revision);
        } else if (m_revision != null && m_prevRevision != null) {
            buffer.append(" has changed from ")
                .append(m_prevRevision).append(" to ").append(m_revision);
        }
        return buffer.toString();
    }
}
