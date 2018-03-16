package org.apache.tools.ant.taskdefs.cvslib;

import java.util.Date;
import java.util.Vector;

/**
 * CVS Entry.
 *
 */
class CVSEntry {
    private Date m_date;
    private String m_author;
    private final String m_comment;
    private final Vector m_files = new Vector();

    public CVSEntry(Date date, String author, String comment) {
        m_date = date;
        m_author = author;
        m_comment = comment;
    }

    public void addFile(String file, String revision) {
        m_files.addElement(new RCSFile(file, revision));
    }

    public void addFile(String file, String revision, String previousRevision) {
        m_files.addElement(new RCSFile(file, revision, previousRevision));
    }

    Date getDate() {
        return m_date;
    }

    void setAuthor(final String author) {
        m_author = author;
    }

    String getAuthor() {
        return m_author;
    }

    String getComment() {
        return m_comment;
    }

    Vector getFiles() {
        return m_files;
    }

    public String toString() {
        return getAuthor() + "\n" + getDate() + "\n" + getFiles() + "\n"
            + getComment();
    }
}
