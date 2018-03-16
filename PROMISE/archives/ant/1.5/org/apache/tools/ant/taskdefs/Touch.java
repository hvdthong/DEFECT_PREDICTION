package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Vector;

/**
 * Touch a file and/or fileset(s); corresponds to the Unix touch command.
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
 *
 * @since Ant 1.1
 *
 * @ant.task category="filesystem"
 */
public class Touch extends Task {

    private File file;              
    private long millis = -1;
    private String dateTime;
    private Vector filesets = new Vector();
    private FileUtils fileUtils;

    public Touch() {
        fileUtils = FileUtils.newFileUtils();
    }

    /**
     * Sets a single source file to touch.  If the file does not exist
     * an empty file will be created.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * the new modification time of the file
     * in milliseconds since midnight Jan 1 1970.
     * Optional, default=now
     */
    public void setMillis(long millis) {
        this.millis = millis;
    }

    /**
     * the new modification time of the file
     * in the format MM/DD/YYYY HH:MM AM <i>or</i> PM;
     * Optional, default=now
     */
    public void setDatetime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Add a set of files to touch
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Execute the touch operation.
     */
    public void execute() throws BuildException {
        long savedMillis = millis;

        if (file == null && filesets.size() == 0) {
            throw 
                new BuildException("Specify at least one source - a file or "
                                   + "a fileset.");
        }

        if (file != null && file.exists() && file.isDirectory()) {
            throw new BuildException("Use a fileset to touch directories.");
        }

        try {
            if (dateTime != null) {
                DateFormat df = 
                    DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                                   DateFormat.SHORT,
                                                   Locale.US);
                try {
                    setMillis(df.parse(dateTime).getTime());
                    if (millis < 0) {
                        throw new BuildException("Date of " + dateTime
                                                 + " results in negative "
                                                 + "milliseconds value "
                                                 + "relative to epoch "
                                                 + "(January 1, 1970, "
                                                 + "00:00:00 GMT).");
                    }
                } catch (ParseException pe) {
                    throw new BuildException(pe.getMessage(), pe, location);
                }
            }

            touch();
        } finally {
            millis = savedMillis;
        }
    }

    /**
     * Does the actual work. Entry point for Untar and Expand as well.
     */
    protected void touch() throws BuildException {
        if (file != null) {
            if (!file.exists()) {
                log("Creating " + file, Project.MSG_INFO);
                try {
                    fileUtils.createNewFile(file);
                } catch (IOException ioe) {
                    throw new BuildException("Could not create " + file, ioe, 
                                             location);
                }
            }
        }

        if (millis >= 0 && 
            JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_1)) {
            log("modification time of files cannot be set in JDK 1.1",
                Project.MSG_WARN);
            return;
        } 

        boolean resetMillis = false;
        if (millis < 0) {
            resetMillis = true;
            millis = System.currentTimeMillis();
        }

        if (file != null) {
            touch(file);
        }

        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(project);
            File fromDir = fs.getDir(project);

            String[] srcFiles = ds.getIncludedFiles();
            String[] srcDirs = ds.getIncludedDirectories();

            for (int j = 0; j < srcFiles.length ; j++) {
                touch(new File(fromDir, srcFiles[j]));
            }
         
            for (int j = 0; j < srcDirs.length ; j++) {
                touch(new File(fromDir, srcDirs[j]));
            }
        }

        if (resetMillis) {
            millis = -1;
        }
    }

    protected void touch(File file) throws BuildException {
        if (!file.canWrite()) {
            throw new BuildException("Can not change modification date of "
                                     + "read-only file " + file);
        }

        if (JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_1)) {
            return;
        }

        fileUtils.setFileLastModified(file, millis);
    }

}
