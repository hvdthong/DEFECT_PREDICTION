package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.tar.*;
import org.apache.tools.ant.types.*;

/**
 * Creates a TAR archive.
 *
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class Tar extends MatchingTask {

    static public final String WARN = "warn";
    static public final String FAIL = "fail";
    static public final String TRUNCATE = "truncate";
    static public final String GNU = "gnu";
    static public final String OMIT = "omit";

    private String[] validModes = new String[] {WARN, FAIL, TRUNCATE, GNU, OMIT};

    File tarFile;
    File baseDir;
    
    String longFileMode = WARN;
    
    Vector filesets = new Vector();
    Vector fileSetFiles = new Vector();
    
    /**
     * Indicates whether the user has been warned about long files already.
     */
    private boolean longWarningGiven = false;

    public TarFileSet createTarFileSet() {
        TarFileSet fileset = new TarFileSet();
        filesets.addElement(fileset);
        return fileset;
    }
    
    
    /**
     * This is the name/location of where to create the tar file.
     */
    public void setTarfile(File tarFile) {
        this.tarFile = tarFile;
    }
    
    /**
     * This is the base directory to look in for things to tar.
     */
    public void setBasedir(File baseDir) {
        this.baseDir = baseDir;
    }
    
    /**
     * Set how to handle long files.
     *
     * Allowable values are
     *   truncate - paths are truncated to the maximum length
     *   fail - patsh greater than the maximim cause a build exception
     *   warn - paths greater than the maximum cause a warning and GNU is used
     *   gnu - GNU extensions are used for any paths greater than the maximum.
     *   omit - paths greater than the maximum are omitted from the archive
     */
    public void setLongfile(String mode) {
        for (int i = 0; i < validModes.length; ++i) {
            if (mode.equalsIgnoreCase(validModes[i])) {
                this.longFileMode = mode;
                return;
            }
        }
        throw new BuildException("The longfile value " + mode + " is not a valid value");
    }

    public void execute() throws BuildException {
        if (tarFile == null) {
            throw new BuildException("tarfile attribute must be set!", 
                                     location);
        }

        if (tarFile.exists() && tarFile.isDirectory()) {
            throw new BuildException("tarfile is a directory!", 
                                     location);
        }

        if (tarFile.exists() && !tarFile.canWrite()) {
            throw new BuildException("Can not write to the specified tarfile!", 
                                     location);
        }

        if (baseDir != null) {
            if (!baseDir.exists()) {
                throw new BuildException("basedir does not exist!", location);
            }
            
            TarFileSet mainFileSet = new TarFileSet(fileset);
            mainFileSet.setDir(baseDir);
            filesets.addElement(mainFileSet);
        }
        
        if (filesets.size() == 0) {
            throw new BuildException("You must supply either a basdir attribute or some nested filesets.", 
                                     location);
        }
        
        boolean upToDate = true;
        for (Enumeration e = filesets.elements(); e.hasMoreElements();) {
            TarFileSet fs = (TarFileSet)e.nextElement();
            String[] files = fs.getFiles(project);
            
            if (!archiveIsUpToDate(files)) {
                upToDate = false;
            }
            
            for (int i = 0; i < files.length; ++i) {
                if (tarFile.equals(new File(fs.getDir(project), files[i]))) {
                    throw new BuildException("A tar file cannot include itself", location);
                }
            }
        }

        if (upToDate) {
            log("Nothing to do: "+tarFile.getAbsolutePath()+" is up to date.",
                Project.MSG_INFO);
            return;
        }

        log("Building tar: "+ tarFile.getAbsolutePath(), Project.MSG_INFO);

        TarOutputStream tOut = null;
        try {
            tOut = new TarOutputStream(new FileOutputStream(tarFile));
            tOut.setDebug(true);
            if (longFileMode.equalsIgnoreCase(TRUNCATE)) {
                tOut.setLongFileMode(TarOutputStream.LONGFILE_TRUNCATE);
            }
            else if (longFileMode.equalsIgnoreCase(FAIL) ||
                     longFileMode.equalsIgnoreCase(OMIT)) {
                tOut.setLongFileMode(TarOutputStream.LONGFILE_ERROR);
            }
            else {
                tOut.setLongFileMode(TarOutputStream.LONGFILE_GNU);
            }
        
            longWarningGiven = false;
            for (Enumeration e = filesets.elements(); e.hasMoreElements();) {
                TarFileSet fs = (TarFileSet)e.nextElement();
                String[] files = fs.getFiles(project);
                for (int i = 0; i < files.length; i++) {
                    File f = new File(fs.getDir(project), files[i]);
                    String name = files[i].replace(File.separatorChar,'/');
                    tarFile(f, tOut, name, fs);
                }
            }
        } catch (IOException ioe) {
            String msg = "Problem creating TAR: " + ioe.getMessage();
            throw new BuildException(msg, ioe, location);
        } finally {
            if (tOut != null) {
                try {
                    tOut.close();
                }
                catch (IOException e) {}
            }
        }
    }

    protected void tarFile(File file, TarOutputStream tOut, String vPath,
                           TarFileSet tarFileSet)
        throws IOException
    {
        FileInputStream fIn = null;

        if (vPath.length() <= 0) {
            return;
        }
        
        if (file.isDirectory() && !vPath.endsWith("/")) {
            vPath += "/";
        }

        try {
            if (vPath.length() >= TarConstants.NAMELEN) {
                if (longFileMode.equalsIgnoreCase(OMIT)) {
                    log("Omitting: "+ vPath, Project.MSG_INFO);
                    return;
                } else if (longFileMode.equalsIgnoreCase(WARN)) {
                    log("Entry: "+ vPath + " longer than " + 
                        TarConstants.NAMELEN + " characters.", Project.MSG_WARN);
                    if (!longWarningGiven) {                        
                        log("Resulting tar file can only be processed successfully"
                            + " by GNU compatible tar commands", Project.MSG_WARN);
                        longWarningGiven = true;
                    }
                } else if (longFileMode.equalsIgnoreCase(FAIL)) {
                    throw new BuildException(
                        "Entry: "+ vPath + " longer than " + 
                        TarConstants.NAMELEN + "characters.", location);
                }
            }

            TarEntry te = new TarEntry(vPath);
            te.setModTime(file.lastModified());
            if (!file.isDirectory()) {
                te.setSize(file.length());
                te.setMode(tarFileSet.getMode());
            } 
            te.setUserName(tarFileSet.getUserName());
            te.setGroupName(tarFileSet.getGroup());
            
            tOut.putNextEntry(te);
            
            if (!file.isDirectory()) {
                fIn = new FileInputStream(file);

                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    tOut.write(buffer, 0, count);
                    count = fIn.read(buffer, 0, buffer.length);
                } while (count != -1);
            }
            
            tOut.closeEntry();        
        } finally {
            if (fIn != null)
                fIn.close();
        }
    }

    protected boolean archiveIsUpToDate(String[] files) {
        SourceFileScanner sfs = new SourceFileScanner(this);
        MergingMapper mm = new MergingMapper();
        mm.setTo(tarFile.getAbsolutePath());
        return sfs.restrict(files, baseDir, null, mm).length == 0;
    }

    static public class TarFileSet extends FileSet {
        private String[] files = null;
        
        private int mode = 0100644;
        
        private String userName = "";
        private String groupName = "";
        
           
        public TarFileSet(FileSet fileset) {
            super(fileset);
        }
        
        public TarFileSet() {
            super();
        }
        
        /**
         *  Get a list of files and directories specified in the fileset.
         *  @return a list of file and directory names, relative to
         *    the baseDir for the project.
         */
        public String[] getFiles(Project p) {
            if (files == null) {
                DirectoryScanner ds = getDirectoryScanner(p);
                String[] directories = ds.getIncludedDirectories();
                String[] filesPerSe = ds.getIncludedFiles();
                files = new String [directories.length + filesPerSe.length];
                System.arraycopy(directories, 0, files, 0, directories.length);
                System.arraycopy(filesPerSe, 0, files, directories.length,
                        filesPerSe.length);
            }
            
            return files;
        }
        
        public void setMode(String octalString) {
            this.mode = 0100000 | Integer.parseInt(octalString, 8);
        }
            
        public int getMode() {
            return mode;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setGroup(String groupName) {
            this.groupName = groupName;
        }
        
        public String getGroup() {
            return groupName;
        }
        
    }
}
