package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

/**
 * Touch a file and/or fileset(s) -- corresponds to the Unix touch command.
 *
 * <p>If the file to touch doesn't exist, an empty one is
 * created. </p>
 *
 * <p>Note: Setting the modification time of files is not supported in
 * JDK 1.1.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author <a href="mailto:mj@servidium.com">Michael J. Sikorsky</a>
 * @author <a href="mailto:shaw@servidium.com">Robert Shaw</a>
 */
public class Touch extends Task {

    private long millis = -1;
    private String dateTime;
    private Vector filesets = new Vector();
    private FileUtils fileUtils;

    public Touch() {
        fileUtils = FileUtils.newFileUtils();
    }

    /**
     * Sets a single source file to touch.  If the file does not exist
     * an  empty file will be created.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Milliseconds since 01/01/1970 00:00 am.
     */
    public void setMillis(long millis) {
        this.millis = millis;
    }

    /**
     * Date in the format MM/DD/YYYY HH:MM AM_PM.
     */
    public void setDatetime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Execute the touch operation.
     */
    public void execute() throws BuildException {
        if (file == null && filesets.size() == 0) {
            throw 
                new BuildException("Specify at least one source - a file or a fileset.");
        }

        if (file != null && file.exists() && file.isDirectory()) {
            throw new BuildException("Use a fileset to touch directories.");
        }

        if (dateTime != null) {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                                           DateFormat.SHORT,
                                                           Locale.US);
            try {
                setMillis(df.parse(dateTime).getTime());
                if (millis < 0) {
                    throw new BuildException("Date of " + dateTime
                                             + " results in negative milliseconds value relative to epoch (January 1, 1970, 00:00:00 GMT).");
                }
            } catch (ParseException pe) {
                throw new BuildException(pe.getMessage(), pe, location);
            }
        }

        touch();
    }

    /**
     * Does the actual work. Entry point for Untar and Expand as well.
     */
    protected void touch() throws BuildException {
        if (file != null) {
            if (!file.exists()) {
                log("Creating "+file, Project.MSG_INFO);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(new byte[0]);
                    fos.close();
                } catch (IOException ioe) {
                    throw new BuildException("Could not create "+file, ioe, 
                                             location);
                }
            }
            touch(file);
        }

        if (millis >= 0 && project.getJavaVersion() == Project.JAVA_1_1) {
            log("modification time of files cannot be set in JDK 1.1",
                Project.MSG_WARN);
            return;
        }

        for (int i=0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(project);
            File fromDir = fs.getDir(project);

            String[] srcFiles = ds.getIncludedFiles();
            String[] srcDirs = ds.getIncludedDirectories();

            for(int j=0; j < srcFiles.length ; j++) {
                touch(new File(fromDir, srcFiles[j]));
            }
         
            for(int j=0; j < srcDirs.length ; j++) {
                touch(new File(fromDir, srcDirs[j]));
            }
        }
    }

    protected void touch(File file) throws BuildException {
        if (!file.canWrite()) {
            throw new BuildException("Can not change modification date of read-only file " + file);
        }

        if (project.getJavaVersion() == Project.JAVA_1_1) {
            return;
        }

        if (millis < 0) {
            fileUtils.setFileLastModified(file, System.currentTimeMillis());
        } else {
            fileUtils.setFileLastModified(file, millis);
        }
    }

}
