package org.apache.tools.ant.types;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.FileResourceIterator;

/**
 * ArchiveScanner accesses the pattern matching algorithm in DirectoryScanner,
 * which are protected methods that can only be accessed by subclassing.
 *
 * This implementation of FileScanner defines getIncludedFiles to return
 * the matching archive entries.
 *
 * @since Ant 1.7
 */
public abstract class ArchiveScanner extends DirectoryScanner {

    /**
     * The archive file which should be scanned.
     */
    protected File srcFile;


    /**
     * The archive resource which should be scanned.
     */
    private Resource src;

    /**
     * to record the last scanned zip file with its modification date
     */
    private Resource lastScannedResource;

    /**
     * record list of all file zip entries
     */
    private TreeMap fileEntries = new TreeMap();

    /**
     * record list of all directory zip entries
     */
    private TreeMap dirEntries = new TreeMap();

    /**
     * record list of matching file zip entries
     */
    private TreeMap matchFileEntries = new TreeMap();

    /**
     * record list of matching directory zip entries
     */
    private TreeMap matchDirEntries = new TreeMap();

    /**
     * encoding of file names.
     *
     * @since Ant 1.6
     */
    private String encoding;

    /**
     * Don't scan when we have no zipfile.
     * @since Ant 1.7
     */
    public void scan() {
        if (src == null) {
            return;
        }
        super.scan();
    }

    /**
     * Sets the srcFile for scanning. This is the jar or zip file that
     * is scanned for matching entries.
     *
     * @param srcFile the (non-null) archive file name for scanning
     */
    public void setSrc(File srcFile) {
        setSrc(new FileResource(srcFile));
    }

    /**
     * Sets the src for scanning. This is the jar or zip file that
     * is scanned for matching entries.
     *
     * @param src the (non-null) archive resource
     */
    public void setSrc(Resource src) {
        this.src = src;
        if (src instanceof FileResource) {
            srcFile = ((FileResource) src).getFile();
        }
    }

    /**
     * Sets encoding of file names.
     * @param encoding the encoding format
     * @since Ant 1.6
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Returns the names of the files which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the files which matched at least one of the
     *         include patterns and none of the exclude patterns.
     */
    public String[] getIncludedFiles() {
        if (src == null) {
            return super.getIncludedFiles();
        }
        scanme();
        Set s = matchFileEntries.keySet();
        return (String[]) (s.toArray(new String[s.size()]));
    }

    /**
     * Override parent implementation.
     * @return count of included files.
     * @since Ant 1.7
     */
    public int getIncludedFilesCount() {
        if (src == null) {
            return super.getIncludedFilesCount();
        }
        scanme();
        return matchFileEntries.size();
    }

    /**
     * Returns the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     * The names are relative to the base directory.
     *
     * @return the names of the directories which matched at least one of the
     * include patterns and none of the exclude patterns.
     */
    public String[] getIncludedDirectories() {
        if (src == null) {
            return super.getIncludedDirectories();
        }
        scanme();
        Set s = matchDirEntries.keySet();
        return (String[]) (s.toArray(new String[s.size()]));
    }

    /**
     * Override parent implementation.
     * @return count of included directories.
     * @since Ant 1.7
     */
    public int getIncludedDirsCount() {
        if (src == null) {
            return super.getIncludedDirsCount();
        }
        scanme();
        return matchDirEntries.size();
    }

    /**
     * Get the set of Resources that represent files.
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    /* package-private for now */ Iterator getResourceFiles() {
        if (src == null) {
            return new FileResourceIterator(getBasedir(), getIncludedFiles());
        }
        scanme();
        return matchFileEntries.values().iterator();
    }

    /**
     * Get the set of Resources that represent directories.
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    /* package-private for now */  Iterator getResourceDirectories() {
        if (src == null) {
            return new FileResourceIterator(getBasedir(), getIncludedDirectories());
        }
        scanme();
        return matchDirEntries.values().iterator();
    }

    /**
     * Initialize DirectoryScanner data structures.
     */
    public void init() {
        if (includes == null) {
            includes = new String[1];
            includes[0] = "**";
        }
        if (excludes == null) {
            excludes = new String[0];
        }
    }

    /**
     * Matches a jar entry against the includes/excludes list,
     * normalizing the path separator.
     *
     * @param path the (non-null) path name to test for inclusion
     *
     * @return <code>true</code> if the path should be included
     *         <code>false</code> otherwise.
     */
    public boolean match(String path) {
        String vpath = path.replace('/', File.separatorChar).
            replace('\\', File.separatorChar);
        return isIncluded(vpath) && !isExcluded(vpath);
    }

    /**
     * Get the named Resource.
     * @param name path name of the file sought in the archive
     * @return the resource
     * @since Ant 1.5.2
     */
    public Resource getResource(String name) {
        if (src == null) {
            return super.getResource(name);
        }
        if (name.equals("")) {
            return new Resource("", true, Long.MAX_VALUE, true);
        }
        scanme();
        if (fileEntries.containsKey(name)) {
            return (Resource) fileEntries.get(name);
        }
        name = trimSeparator(name);

        if (dirEntries.containsKey(name)) {
            return (Resource) dirEntries.get(name);
        }
        return new Resource(name);
    }

    /**
     * Fills the file and directory maps with resources read from the archive.
     *
     * @param archive the archive to scan.
     * @param encoding encoding used to encode file names inside the archive.
     * @param fileEntries Map (name to resource) of non-directory
     * resources found inside the archive.
     * @param matchFileEntries Map (name to resource) of non-directory
     * resources found inside the archive that matched all include
     * patterns and didn't match any exclude patterns.
     * @param dirEntries Map (name to resource) of directory
     * resources found inside the archive.
     * @param matchDirEntries Map (name to resource) of directory
     * resources found inside the archive that matched all include
     * patterns and didn't match any exclude patterns.
     */
    protected abstract void fillMapsFromArchive(Resource archive,
                                                String encoding,
                                                Map fileEntries,
                                                Map matchFileEntries,
                                                Map dirEntries,
                                                Map matchDirEntries);

    /**
     * if the datetime of the archive did not change since
     * lastScannedResource was initialized returns immediately else if
     * the archive has not been scanned yet, then all the zip entries
     * are put into the appropriate tables.
     */
    private void scanme() {
        Resource thisresource = new Resource(src.getName(),
                                             src.isExists(),
                                             src.getLastModified());
        if (lastScannedResource != null
            && lastScannedResource.getName().equals(thisresource.getName())
            && lastScannedResource.getLastModified()
            == thisresource.getLastModified()) {
            return;
        }
        init();

        fileEntries.clear();
        dirEntries.clear();
        matchFileEntries.clear();
        matchDirEntries.clear();
        fillMapsFromArchive(src, encoding, fileEntries, matchFileEntries,
                            dirEntries, matchDirEntries);

        lastScannedResource = thisresource;
    }

    /**
     * Remove trailing slash if present.
     * @param s the file name to trim.
     * @return the trimed file name.
     */
    protected static final String trimSeparator(String s) {
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }

}
