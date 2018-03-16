package org.apache.tools.ant.taskdefs.cvslib;

/**
 * Holds the information of a line of rdiff
 */
public class CvsTagEntry {

    /** the filename */
    private String filename;

    /** the previous revision */
    private String prevRevision;

    /** the revision */
    private String revision;

    /**
     * Creates a new CvsTagEntry
     * @param filename the filename to add
     */
    public CvsTagEntry(final String filename) {
        this(filename, null, null);
    }

    /**
     * Creates a new CvsTagEntry
     * @param filename the filename to add
     * @param revision the revision
     */
    public CvsTagEntry(final String filename, final String revision) {
        this(filename, revision, null);
    }

    /**
     * Creates a new CvsTagEntry
     * @param filename the filename to add
     * @param revision the revision
     * @param prevRevision the previous revision
     */
    public CvsTagEntry(final String filename, final String revision,
                       final String prevRevision) {
        this.filename = filename;
        this.revision = revision;
        this.prevRevision = prevRevision;
    }

    /**
     * Gets the filename for this CvsTagEntry
     * @return the filename
     */
    public String getFile() {
        return filename;
    }

    /**
     * Gets the revision for this CvsTagEntry
     * @return the revision
     */
    public String getRevision() {
        return revision;
    }

    /**
     * Gets the previous revision for this CvsTagEntry
     * @return the previous revision
     */
    public String getPreviousRevision() {
        return prevRevision;
    }

    /**
     * Gets a String containing filename and difference from previous version
     * @return a string representation of this CVSTagEntry
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(filename);
        if (revision == null) {
            buffer.append(" was removed");
            if (prevRevision != null) {
                buffer.append("; previous revision was ").append(prevRevision);
            }
        } else if (prevRevision == null) {
            buffer.append(" is new; current revision is ")
                .append(revision);
        } else {
            buffer.append(" has changed from ")
                .append(prevRevision).append(" to ").append(revision);
        }
        return buffer.toString();
    }
}
