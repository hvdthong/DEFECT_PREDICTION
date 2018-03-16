package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.StreamPumper;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;


/**
 * Create a CAB archive.
 *
 */

public class Cab extends MatchingTask {
    private static final int DEFAULT_RESULT = -99;
    private File cabFile;
    private File baseDir;
    private Vector filesets = new Vector();
    private boolean doCompress = true;
    private boolean doVerbose = false;
    private String cmdOptions;

    protected String archiveType = "cab";

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * The name/location of where to create the .cab file.
     * @param cabFile the location of the cab file.
     */
    public void setCabfile(File cabFile) {
        this.cabFile = cabFile;
    }

    /**
     * Base directory to look in for files to CAB.
     * @param baseDir base directory for files to cab.
     */
    public void setBasedir(File baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * If true, compress the files otherwise only store them.
     * @param compress a <code>boolean</code> value.
     */
    public void setCompress(boolean compress) {
        doCompress = compress;
    }

    /**
     * If true, display cabarc output.
     * @param verbose a <code>boolean</code> value.
     */
    public void setVerbose(boolean verbose) {
        doVerbose = verbose;
    }

    /**
     * Sets additional cabarc options that are not supported directly.
     * @param options cabarc command line options.
     */
    public void setOptions(String options) {
        cmdOptions = options;
    }

    /**
     * Adds a set of files to archive.
     * @param set a set of files to archive.
     */
    public void addFileset(FileSet set) {
        if (filesets.size() > 0) {
            throw new BuildException("Only one nested fileset allowed");
        }
        filesets.addElement(set);
    }

    /*
     * I'm not fond of this pattern: "sub-method expected to throw
     * task-cancelling exceptions".  It feels too much like programming
     * for side-effects to me...
     */
    /**
     * Check if the attributes and nested elements are correct.
     * @throws BuildException on error.
     */
    protected void checkConfiguration() throws BuildException {
        if (baseDir == null && filesets.size() == 0) {
            throw new BuildException("basedir attribute or one "
                                     + "nested fileset is required!",
                                     getLocation());
        }
        if (baseDir != null && !baseDir.exists()) {
            throw new BuildException("basedir does not exist!", getLocation());
        }
        if (baseDir != null && filesets.size() > 0) {
            throw new BuildException(
                "Both basedir attribute and a nested fileset is not allowed");
        }
        if (cabFile == null) {
            throw new BuildException("cabfile attribute must be set!",
                                     getLocation());
        }
    }

    /**
     * Create a new exec delegate.  The delegate task is populated so that
     * it appears in the logs to be the same task as this one.
     * @return the delegate.
     * @throws BuildException on error.
     */
    protected ExecTask createExec() throws BuildException {
        ExecTask exec = new ExecTask(this);
        return exec;
    }

    /**
     * Check to see if the target is up to date with respect to input files.
     * @param files the list of files to check.
     * @return true if the cab file is newer than its dependents.
     */
    protected boolean isUpToDate(Vector files) {
        boolean upToDate = true;
        for (int i = 0; i < files.size() && upToDate; i++) {
            String file = files.elementAt(i).toString();
            if (FILE_UTILS.resolveFile(baseDir, file).lastModified()
                    > cabFile.lastModified()) {
                upToDate = false;
            }
        }
        return upToDate;
    }

    /**
     * Creates a list file.  This temporary file contains a list of all files
     * to be included in the cab, one file per line.
     *
     * <p>This method expects to only be called on Windows and thus
     * quotes the file names.</p>
     * @param files the list of files to use.
     * @return the list file created.
     * @throws IOException if there is an error.
     */
    protected File createListFile(Vector files)
        throws IOException {
        File listFile = FILE_UTILS.createTempFile("ant", "", null, true, true);

        PrintWriter writer = new PrintWriter(new FileOutputStream(listFile));

        int size = files.size();
        for (int i = 0; i < size; i++) {
            writer.println('\"' + files.elementAt(i).toString() + '\"');
        }
        writer.close();

        return listFile;
    }

    /**
     * Append all files found by a directory scanner to a vector.
     * @param files the vector to append the files to.
     * @param ds the scanner to get the files from.
     */
    protected void appendFiles(Vector files, DirectoryScanner ds) {
        String[] dsfiles = ds.getIncludedFiles();

        for (int i = 0; i < dsfiles.length; i++) {
            files.addElement(dsfiles[i]);
        }
    }

    /**
     * Get the complete list of files to be included in the cab.  Filenames
     * are gathered from the fileset if it has been added, otherwise from the
     * traditional include parameters.
     * @return the list of files.
     * @throws BuildException if there is an error.
     */
    protected Vector getFileList() throws BuildException {
        Vector files = new Vector();

        if (baseDir != null) {
            appendFiles(files, super.getDirectoryScanner(baseDir));
        } else {
            FileSet fs = (FileSet) filesets.elementAt(0);
            baseDir = fs.getDir();
            appendFiles(files, fs.getDirectoryScanner(getProject()));
        }

        return files;
    }

    /**
     * execute this task.
     * @throws BuildException on error.
     */
    public void execute() throws BuildException {

        checkConfiguration();

        Vector files = getFileList();

        if (isUpToDate(files)) {
            return;
        }

        log("Building " + archiveType + ": " + cabFile.getAbsolutePath());

        if (!Os.isFamily("windows")) {
            log("Using listcab/libcabinet", Project.MSG_VERBOSE);

            StringBuffer sb = new StringBuffer();

            Enumeration fileEnum = files.elements();

            while (fileEnum.hasMoreElements()) {
                sb.append(fileEnum.nextElement()).append("\n");
            }
            sb.append("\n").append(cabFile.getAbsolutePath()).append("\n");

            try {
                Process p = Execute.launch(getProject(),
                                           new String[] {"listcab"}, null,
                                           baseDir != null ? baseDir
                                                   : getProject().getBaseDir(),
                                           true);
                OutputStream out = p.getOutputStream();

                LogOutputStream outLog = new LogOutputStream(this, Project.MSG_VERBOSE);
                LogOutputStream errLog = new LogOutputStream(this, Project.MSG_ERR);
                StreamPumper    outPump = new StreamPumper(p.getInputStream(), outLog);
                StreamPumper    errPump = new StreamPumper(p.getErrorStream(), errLog);

                (new Thread(outPump)).start();
                (new Thread(errPump)).start();

                out.write(sb.toString().getBytes());
                out.flush();
                out.close();

                int result = DEFAULT_RESULT;

                try {
                    result = p.waitFor();

                    outPump.waitFor();
                    outLog.close();
                    errPump.waitFor();
                    errLog.close();
                } catch (InterruptedException ie) {
                    log("Thread interrupted: " + ie);
                }

                if (Execute.isFailure(result)) {
                    log("Error executing listcab; error code: " + result);
                }
            } catch (IOException ex) {
                String msg = "Problem creating " + cabFile + " " + ex.getMessage();
                throw new BuildException(msg, getLocation());
            }
        } else {
            try {
                File listFile = createListFile(files);
                ExecTask exec = createExec();
                File outFile = null;

                exec.setFailonerror(true);
                exec.setDir(baseDir);

                if (!doVerbose) {
                    outFile = FILE_UTILS.createTempFile("ant", "", null, true, true);
                    exec.setOutput(outFile);
                }

                exec.setExecutable("cabarc");
                exec.createArg().setValue("-r");
                exec.createArg().setValue("-p");

                if (!doCompress) {
                    exec.createArg().setValue("-m");
                    exec.createArg().setValue("none");
                }

                if (cmdOptions != null) {
                    exec.createArg().setLine(cmdOptions);
                }

                exec.createArg().setValue("n");
                exec.createArg().setFile(cabFile);
                exec.createArg().setValue("@" + listFile.getAbsolutePath());

                exec.execute();

                if (outFile != null) {
                    outFile.delete();
                }

                listFile.delete();
            } catch (IOException ioe) {
                String msg = "Problem creating " + cabFile + " " + ioe.getMessage();
                throw new BuildException(msg, getLocation());
            }
        }
    }
}
