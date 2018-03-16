package org.apache.tools.ant.taskdefs.cvslib;

/**
 * Represents a RCS File change.
 *
 */
class RCSFile {
    private String name;
    private String revision;
    private String previousRevision;


    RCSFile(final String name, final String rev) {
        this(name, rev, null);
    }


    RCSFile(final String name,
                  final String revision,
                  final String previousRevision) {
        this.name = name;
        this.revision = revision;
        if (!revision.equals(previousRevision)) {
            this.previousRevision = previousRevision;
        }
    }

    /**
     * Gets the name of the RCSFile
     * @return name of the file
     */
    String getName() {
        return name;
    }

    /**
     * Gets the revision number of the RCSFile
     * @return the revision number (as String)
     */
    String getRevision() {
        return revision;
    }

    /**
     * Gets the previous revision of the RCSFile
     * @return the previous revision number (as String)
     */
    String getPreviousRevision() {
        return previousRevision;
    }
}

