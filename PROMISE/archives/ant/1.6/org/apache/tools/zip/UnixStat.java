package org.apache.tools.zip;

/**
 * Constants from stat.h on Unix systems.
 *
 */
public interface UnixStat {

    /**
     * Bits used for permissions (and sticky bit)
     *
     * @since 1.1
     */
    int PERM_MASK =           07777;
    /**
     * Indicates symbolic links.
     *
     * @since 1.1
     */
    int LINK_FLAG =         0120000;
    /**
     * Indicates plain files.
     *
     * @since 1.1
     */
    int FILE_FLAG =         0100000;
    /**
     * Indicates directories.
     *
     * @since 1.1
     */
    int DIR_FLAG =           040000;


    /**
     * Default permissions for symbolic links.
     *
     * @since 1.1
     */
    int DEFAULT_LINK_PERM =    0777;
    /**
     * Default permissions for directories.
     *
     * @since 1.1
     */
    int DEFAULT_DIR_PERM =     0755;
    /**
     * Default permissions for plain files.
     *
     * @since 1.1
     */
    int DEFAULT_FILE_PERM =    0644;
}
