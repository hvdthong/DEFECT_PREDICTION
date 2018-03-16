package org.apache.tools.ant.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;

/**
 * ZipScanner accesses the pattern matching algorithm in DirectoryScanner,
 * which are protected methods that can only be accessed by subclassing.
 *
 * This implementation of FileScanner defines getIncludedFiles to return
 * the matching Zip entries.
 *
 * @author Don Ferguson <a href="mailto:don@bea.com">don@bea.com</a>
 * @author <a href="mailto:levylambert@tiscali-dsl.de">Antoine Levy-Lambert</a>
 */
public class ZipScanner extends DirectoryScanner {

    /**
     * The zip file which should be scanned.
     */
    protected File srcFile;
    /**
     * to record the last scanned zip file with its modification date
     */
    private Resource lastScannedResource;
    /**
     * record list of all zip entries
     */
    private Hashtable myentries;

    /**
     * Sets the srcFile for scanning. This is the jar or zip file that
     * is scanned for matching entries.
     *
     * @param srcFile the (non-null) zip file name for scanning
     */
    public void setSrc(File srcFile) {
        this.srcFile = srcFile;
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
        if (srcFile != null) {
            Vector myvector = new Vector();
            scanme();
            for (Enumeration e = myentries.elements(); e.hasMoreElements() ;) {
                Resource myresource= (Resource) e.nextElement();
                if (!myresource.isDirectory() && match(myresource.getName())) {
                    myvector.addElement(myresource.getName());
                }
            }
            String[] files = new String[myvector.size()];
            myvector.copyInto(files);
            return files;
        } else {
            return super.getIncludedFiles();
        }
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
        if (srcFile != null) {
            Vector myvector=new Vector();
            scanme();
            for (Enumeration e = myentries.elements(); e.hasMoreElements() ;) {
                Resource myresource= (Resource) e.nextElement();
                if (myresource.isDirectory() && match(myresource.getName())) {
                    myvector.addElement(myresource.getName());
                }
            }
            String[] files = new String[myvector.size()];
            myvector.copyInto(files);
            return files;
        } else {
            return super.getIncludedDirectories();
        }
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
     * @param name path name of the file sought in the archive
     *
     * @since Ant 1.5.2
     */
    public Resource getResource(String name) {
        if (srcFile == null) {
            return super.getResource(name);
        } else if (name.equals("")) {
            return new Resource("", true, Long.MAX_VALUE, true);
        }

        scanme();
        if (myentries.containsKey(name)) {
            return (Resource) myentries.get(name);
        } else if (myentries.containsKey(name + "/")) {
            return (Resource) myentries.get(name + "/");
        } else {
            return new Resource(name);
        }
    }

    /**
     * if the datetime of the archive did not change since
     * lastScannedResource was initialized returns immediately else if
     * the archive has not been scanned yet, then all the zip entries
     * are put into the vector myentries as a vector of the resource
     * type
     */
    private void scanme() {
        Resource thisresource = new Resource(srcFile.getAbsolutePath(),
                                             srcFile.exists(),
                                             srcFile.lastModified());

        if (lastScannedResource != null
            && lastScannedResource.getName().equals(thisresource.getName())
            && lastScannedResource.getLastModified()
            == thisresource.getLastModified()) {
            return;
        }

        ZipEntry entry = null;
        ZipInputStream in = null;
        myentries = new Hashtable();
        try {
            try {
                in = new ZipInputStream(new FileInputStream(srcFile));
            } catch (IOException ex) {
                throw new BuildException("problem opening " + srcFile, ex);
            }

            while (true) {
                try {
                    entry = in.getNextEntry();
                    if (entry == null) {
                        break;
                    }
                    myentries.put(new String(entry.getName()),
                                  new Resource(entry.getName(), true,
                                               entry.getTime(), 
                                               entry.isDirectory()));
                } catch (ZipException ex) {
                    throw new BuildException("problem reading " + srcFile,
                                             ex);
                } catch (IOException e) {
                    throw new BuildException("problem reading zip entry from " 
                                             + srcFile, e);
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
        lastScannedResource = thisresource;
    }
}
