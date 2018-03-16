package org.apache.tools.tar;

/**
 * This interface contains all the definitions used in the package.
 *
 * @author Timothy Gerard Endres <a href="mailto:time@ice.com">time@ice.com</a>
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 */

public interface TarConstants {
    
    /**
     * The length of the name field in a header buffer.
     */
    public static final int    NAMELEN = 100;

    /**
     * The length of the mode field in a header buffer.
     */
    public static final int    MODELEN = 8;

    /**
     * The length of the user id field in a header buffer.
     */
    public static final int    UIDLEN = 8;

    /**
     * The length of the group id field in a header buffer.
     */
    public static final int    GIDLEN = 8;

    /**
     * The length of the checksum field in a header buffer.
     */
    public static final int    CHKSUMLEN = 8;

    /**
     * The length of the size field in a header buffer.
     */
    public static final int    SIZELEN = 12;

    /**
     * The length of the magic field in a header buffer.
     */
    public static final int    MAGICLEN = 8;

    /**
     * The length of the modification time field in a header buffer.
     */
    public static final int    MODTIMELEN = 12;

    /**
     * The length of the user name field in a header buffer.
     */
    public static final int    UNAMELEN = 32;

    /**
     * The length of the group name field in a header buffer.
     */
    public static final int    GNAMELEN = 32;

    /**
     * The length of the devices field in a header buffer.
     */
    public static final int    DEVLEN = 8;

    /**
     * LF_ constants represent the "link flag" of an entry, or more commonly,
     * the "entry type". This is the "old way" of indicating a normal file.
     */
    public static final byte   LF_OLDNORM = 0;

    /**
     * Normal file type.
     */
    public static final byte   LF_NORMAL = (byte) '0';

    /**
     * Link file type.
     */
    public static final byte   LF_LINK = (byte) '1';

    /**
     * Symbolic link file type.
     */
    public static final byte   LF_SYMLINK = (byte) '2';

    /**
     * Character device file type.
     */
    public static final byte   LF_CHR = (byte) '3';

    /**
     * Block device file type.
     */
    public static final byte   LF_BLK = (byte) '4';

    /**
     * Directory file type.
     */
    public static final byte   LF_DIR = (byte) '5';

    /**
     * FIFO (pipe) file type.
     */
    public static final byte   LF_FIFO = (byte) '6';

    /**
     * Contiguous file type.
     */
    public static final byte   LF_CONTIG = (byte) '7';

    /**
     * The magic tag representing a POSIX tar archive.
     */
    public static final String TMAGIC = "ustar";

    /**
     * The magic tag representing a GNU tar archive.
     */
    public static final String GNU_TMAGIC = "ustar  ";

    /**
     * The namr of the GNU tar entry which contains a long name.
     */
    public static final String GNU_LONGLINK = "././@LongLink";
    
    /**
     * Identifies the *next* file on the tape as having a long name.  
     */
    public static final byte LF_GNUTYPE_LONGNAME = (byte) 'L';
}
