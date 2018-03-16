package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

/**
 * Create a ZIP archive.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Zip extends MatchingTask {

    private File zipFile;
    private File baseDir;
    private boolean doCompress = true;
    protected String archiveType = "zip";
    private static long emptyCrc = new CRC32 ().getValue ();
    protected String emptyBehavior = "skip";
    private Vector filesets = new Vector ();
    private Hashtable addedDirs = new Hashtable();

    /**
     * This is the name/location of where to 
     * create the .zip file.
     */
    public void setZipfile(File zipFile) {
        this.zipFile = zipFile;
    }
    
    /**
     * This is the base directory to look in for 
     * things to zip.
     */
    public void setBasedir(File baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Sets whether we want to compress the files or only store them.
     */
    public void setCompress(boolean c) {
        doCompress = c;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Adds a set of files (nested zipfileset attribute) that can be
     * read from an archive and be given a prefix/fullpath.
     */
    public void addZipfileset(ZipFileSet set) {
        filesets.addElement(set);
    }


    /**
     * Sets behavior of the task when no files match.
     * Possible values are: <code>fail</code> (throw an exception
     * and halt the build); <code>skip</code> (do not create
     * any archive, but issue a warning); <code>create</code>
     * (make an archive with no entries).
     * Default for zip tasks is <code>skip</code>;
     * for jar tasks, <code>create</code>.
     */
    public void setWhenempty(String we) throws BuildException {
        we = we.toLowerCase();
        if (!"fail".equals(we) && !"skip".equals(we) && !"create".equals(we))
            throw new BuildException("Unrecognized whenempty attribute: " + we);
        emptyBehavior = we;
    }

    public void execute() throws BuildException {
        if (baseDir == null && filesets.size() == 0 && "zip".equals(archiveType)) {
            throw new BuildException( "basedir attribute must be set, or at least " + 
                                      "one fileset must be given!" );
        }

        if (zipFile == null) {
            throw new BuildException("You must specify the " + archiveType + " file to create!");
        }

        Vector dss = new Vector ();
        if (baseDir != null)
            dss.addElement(getDirectoryScanner(baseDir));
        for (int i=0; i<filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            dss.addElement (fs.getDirectoryScanner(project));
        }
        int dssSize = dss.size();
        FileScanner[] scanners = new FileScanner[dssSize];
        dss.copyInto(scanners);

        if (isUpToDate(scanners, zipFile)) return;

        log("Building "+ archiveType +": "+ zipFile.getAbsolutePath());

        try {
            boolean success = false;
            ZipOutputStream zOut = 
              new ZipOutputStream(new FileOutputStream(zipFile));
            try {
                if (doCompress) {
                    zOut.setMethod(ZipOutputStream.DEFLATED);
                } else {
                    zOut.setMethod(ZipOutputStream.STORED);
                }
                initZipOutputStream(zOut);

                if (baseDir != null)
                    addFiles(getDirectoryScanner(baseDir), zOut, "", "");
                addFiles(filesets, zOut);
                success = true;
            } finally {
                try {
                    if (zOut != null)
                        zOut.close ();
                } catch(IOException ex) {
                    if (success)
                        throw ex;
                }
            }
        } catch (IOException ioe) {
            String msg = "Problem creating " + archiveType + ": " + ioe.getMessage();

            if (!zipFile.delete()) {
                msg += " (and the archive is probably corrupt but I could not delete it)";
            }

            throw new BuildException(msg, ioe, location);
        } finally {
            cleanUp();
        }
    }

    /**
     * Add all files of the given FileScanner to the ZipOutputStream
     * prependig the given prefix to each filename.
     *
     * <p>Ensure parent directories have been added as well.  
     */
    protected void addFiles(FileScanner scanner, ZipOutputStream zOut, 
                            String prefix, String fullpath) throws IOException {
        if (prefix.length() > 0 && fullpath.length() > 0)
             throw new BuildException("Both prefix and fullpath attributes may not be set on the same fileset.");

        File thisBaseDir = scanner.getBasedir();

        String[] dirs = scanner.getIncludedDirectories();
        if (dirs.length > 0 && fullpath.length() > 0)
            throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
        for (int i = 0; i < dirs.length; i++) {
            String name = dirs[i].replace(File.separatorChar,'/');
            if (!name.endsWith("/")) {
                name += "/";
            }
            addParentDirs(thisBaseDir, name, zOut, prefix);
        }

        String[] files = scanner.getIncludedFiles();
         if (files.length > 1 && fullpath.length() > 0)
            throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
        for (int i = 0; i < files.length; i++) {
            File f = new File(thisBaseDir, files[i]);
            if (fullpath.length() > 0)
            {
                addParentDirs(null, fullpath, zOut, "");
                zipFile(f, zOut, fullpath);
            }
            else
            {
                String name = files[i].replace(File.separatorChar,'/');
                addParentDirs(thisBaseDir, name, zOut, prefix);
                zipFile(f, zOut, prefix+name);
            }
        }
    }

    protected void addZipEntries(ZipFileSet fs, DirectoryScanner ds,
      ZipOutputStream zOut, String prefix)
        throws IOException
    {
        ZipScanner zipScanner = (ZipScanner) ds;
        File zipSrc = fs.getSrc();

        ZipEntry entry;
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipSrc));
        while ((entry = in.getNextEntry()) != null) {
            String vPath = entry.getName();
            if (zipScanner.match(vPath)) {
                addParentDirs(null, vPath, zOut, prefix);
                if (! entry.isDirectory()) {
                  zipFile(in, zOut, prefix+vPath, entry.getTime());
                }
            }
        }
    }

    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException
    {
    }

    /**
     * Check whether the archive is up-to-date; and handle behavior for empty archives.
     * @param scanners list of prepared scanners containing files to archive
     * @param zipFile intended archive file (may or may not exist)
     * @return true if nothing need be done (may have done something already); false if
     *         archive creation should proceed
     * @exception BuildException if it likes
     */
    protected boolean isUpToDate(FileScanner[] scanners, File zipFile) throws BuildException
    {
        String[][] fileNames = grabFileNames(scanners);
        File[] files = grabFiles(scanners, fileNames);
        if (files.length == 0) {
            if (emptyBehavior.equals("skip")) {
                log("Warning: skipping "+archiveType+" archive " + zipFile +
                    " because no files were included.", Project.MSG_WARN);
                return true;
            } else if (emptyBehavior.equals("fail")) {
                throw new BuildException("Cannot create "+archiveType+" archive " + zipFile +
                                         ": no files were included.", location);
            } else {
                if (zipFile.exists()) return true;
                log("Note: creating empty "+archiveType+" archive " + zipFile, Project.MSG_INFO);
                try {
                    OutputStream os = new FileOutputStream(zipFile);
                    try {
                        byte[] empty = new byte[22];
                        empty[2] = 5;
                        empty[3] = 6;
                        os.write(empty);
                    } finally {
                        os.close();
                    }
                } catch (IOException ioe) {
                    throw new BuildException("Could not create empty ZIP archive", ioe, location);
                }
                return true;
            }
        } else {
            for (int i = 0; i < files.length; ++i) {
                if (files[i].equals(zipFile)) {
                    throw new BuildException("A zip file cannot include itself", location);
                }
            }

            if (!zipFile.exists()) return false;

            SourceFileScanner sfs = new SourceFileScanner(this);
            MergingMapper mm = new MergingMapper();
            mm.setTo(zipFile.getAbsolutePath());
            for (int i=0; i<scanners.length; i++) {
                if (sfs.restrict(fileNames[i], scanners[i].getBasedir(), null,
                                 mm).length > 0) {
                    return false;
                }
            }
            return true;
        }
    }

    protected static File[] grabFiles(FileScanner[] scanners) {
        return grabFiles(scanners, grabFileNames(scanners));
    }

    protected static File[] grabFiles(FileScanner[] scanners, 
                                      String[][] fileNames) {
        Vector files = new Vector();
        for (int i = 0; i < fileNames.length; i++) {
            File thisBaseDir = scanners[i].getBasedir();
            for (int j = 0; j < fileNames[i].length; j++)
                files.addElement(new File(thisBaseDir, fileNames[i][j]));
        }
        File[] toret = new File[files.size()];
        files.copyInto(toret);
        return toret;
    }

    protected static String[][] grabFileNames(FileScanner[] scanners) {
        String[][] result = new String[scanners.length][];
        for (int i=0; i<scanners.length; i++) {
            String[] files = scanners[i].getIncludedFiles();
            String[] dirs = scanners[i].getIncludedDirectories();
            result[i] = new String[files.length + dirs.length];
            System.arraycopy(files, 0, result[i], 0, files.length);
            System.arraycopy(dirs, 0, result[i], files.length, dirs.length);
        }
        return result;
    }

    protected void zipDir(File dir, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if (addedDirs.get(vPath) != null) {
            return;
        }
        addedDirs.put(vPath, vPath);
        
        ZipEntry ze = new ZipEntry (vPath);
        if (dir != null) ze.setTime (dir.lastModified ());
        ze.setSize (0);
        ze.setMethod (ZipEntry.STORED);
        ze.setCrc (emptyCrc);
        zOut.putNextEntry (ze);
    }

    protected void zipFile(InputStream in, ZipOutputStream zOut, String vPath,
                           long lastModified)
        throws IOException
    {
        ZipEntry ze = new ZipEntry(vPath);
        ze.setTime(lastModified);

        /*
         * XXX ZipOutputStream.putEntry expects the ZipEntry to know its
         * size and the CRC sum before you start writing the data when using 
         * STORED mode.
         *
         * This forces us to process the data twice.
         *
         * I couldn't find any documentation on this, just found out by try 
         * and error.
         */
        if (!doCompress) {
            long size = 0;
            CRC32 cal = new CRC32();
            if (!in.markSupported()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    size += count;
                    cal.update(buffer, 0, count);
                    bos.write(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);
                in = new ByteArrayInputStream(bos.toByteArray());

            } else {
                in.mark(Integer.MAX_VALUE);
                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    size += count;
                    cal.update(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);
                in.reset();
            }
            ze.setSize(size);
            ze.setCrc(cal.getValue());
        }

        zOut.putNextEntry(ze);

        byte[] buffer = new byte[8 * 1024];
        int count = 0;
        do {
            zOut.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
    }

    protected void zipFile(File file, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if (file.equals(zipFile)) {
            throw new BuildException("A zip file cannot include itself", location);
        }

        FileInputStream fIn = new FileInputStream(file);
        try {
            zipFile(fIn, zOut, vPath, file.lastModified());
        } finally {
            fIn.close();
        }
    }

    /**
     * Ensure all parent dirs of a given entry have been added.
     */
    protected void addParentDirs(File baseDir, String entry,
                                 ZipOutputStream zOut, String prefix)
        throws IOException {

        Stack directories = new Stack();
        int slashPos = entry.length();

        while ((slashPos = entry.lastIndexOf((int)'/', slashPos-1)) != -1) {
            String dir = entry.substring(0, slashPos+1);
            if (addedDirs.get(prefix+dir) != null) {
                break;
            }
            directories.push(dir);
        }

        while (!directories.isEmpty()) {
            String dir = (String) directories.pop();
            File f = null;
            if (baseDir != null) {
                f = new File(baseDir, dir);
            } else {
                f = new File(dir);
            }
            zipDir(f, zOut, prefix+dir);
        }
    }

    /**
     * Iterate over the given Vector of (zip)filesets and add
     * all files to the ZipOutputStream using the given prefix.
     */
    protected void addFiles(Vector filesets, ZipOutputStream zOut)
        throws IOException {
        for (int i = 0; i<filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(project);

            String prefix = "";
            String fullpath = "";
            if (fs instanceof ZipFileSet) {
                ZipFileSet zfs = (ZipFileSet) fs;
                prefix = zfs.getPrefix();
                fullpath = zfs.getFullpath();
            }
            
            if (prefix.length() > 0 
                && !prefix.endsWith("/")
                && !prefix.endsWith("\\")) {
                prefix += "/";
            }

            if (prefix.length() > 0) {
                addParentDirs(null, prefix, zOut, "");
                zipDir(null, zOut, prefix);
            } else if (fullpath.length() > 0) {
                addParentDirs(null, fullpath, zOut, "");
            }

            if (fs instanceof ZipFileSet
                && ((ZipFileSet) fs).getSrc() != null) {
                addZipEntries((ZipFileSet) fs, ds, zOut, prefix);
            } else {
                addFiles(ds, zOut, prefix, fullpath);
            }
        }
    }

    /**
     * Do any clean up necessary to allow this instance to be used again.
     *
     * <p>When we get here, the Zip file has been closed and all we
     * need to do is to reset some globals.</p>
     */
    protected void cleanUp() {}
}
