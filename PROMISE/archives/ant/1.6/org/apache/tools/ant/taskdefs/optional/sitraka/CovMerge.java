package org.apache.tools.ant.taskdefs.optional.sitraka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;

/**
 * Runs the snapshot merge utility for JProbe Coverage.
 *
 * @ant.task name="jpcovmerge" category="metrics"
 */
public class CovMerge extends CovBase {

    /** the name of the output snapshot */
    private File tofile = null;

    /** the filesets that will get all snapshots to merge */
    private Vector filesets = new Vector();

    private boolean verbose;

    /**
     * Set the output snapshot file.
     */
    public void setTofile(File value) {
        this.tofile = value;
    }

    /**
     * If true, perform the merge in verbose mode giving details
     * about the snapshot processing.
     */
    public void setVerbose(boolean flag) {
        this.verbose = flag;
    }

    /**
     * add a fileset containing the snapshots to include.
     */
    public void addFileset(FileSet fs) {
        filesets.addElement(fs);
    }


    public CovMerge() {
    }

    /** execute the jpcovmerge by providing a parameter file */
    public void execute() throws BuildException {
        checkOptions();

        File paramfile = createParamFile();
        try {
            Commandline cmdl = new Commandline();
            cmdl.setExecutable(findExecutable("jpcovmerge"));
            if (verbose) {
                cmdl.createArgument().setValue("-v");
            }
            cmdl.createArgument().setValue(getParamFileArgument()
                                           + paramfile.getAbsolutePath());

            if (isJProbe4Plus()) {
                cmdl.createArgument().setValue(tofile.getPath());
            }

            LogStreamHandler handler
                = new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN);
            Execute exec = new Execute(handler);
            log(cmdl.describeCommand(), Project.MSG_VERBOSE);
            exec.setCommandline(cmdl.getCommandline());

            int exitValue = exec.execute();
            if (Execute.isFailure(exitValue)) {
                throw new BuildException("JProbe Coverage Merging failed (" + exitValue + ")");
            }
        } catch (IOException e) {
            throw new BuildException("Failed to run JProbe Coverage Merge: " + e);
        } finally {
            paramfile.delete();
        }
    }

    /** check for mandatory options */
    protected void checkOptions() throws BuildException {
        if (tofile == null) {
            throw new BuildException("'tofile' attribute must be set.");
        }

        if (getHome() == null || !getHome().isDirectory()) {
            throw new BuildException("Invalid home directory. Must point to JProbe home directory");
        }
        File jar = findCoverageJar();
        if (!jar.exists()) {
            throw new BuildException("Cannot find Coverage directory: " + getHome());
        }
    }

    /** get the snapshots from the filesets */
    protected File[] getSnapshots() {
        Vector v = new Vector();
        final int size = filesets.size();
        for (int i = 0; i < size; i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            ds.scan();
            String[] f = ds.getIncludedFiles();
            for (int j = 0; j < f.length; j++) {
                String pathname = f[j];
                File file = new File(ds.getBasedir(), pathname);
                file = getProject().resolveFile(file.getPath());
                v.addElement(file);
            }
        }

        File[] files = new File[v.size()];
        v.copyInto(files);
        return files;
    }


    /**
     * create the parameters file that contains all file to merge
     * and the output filename.
     */
    protected File createParamFile() throws BuildException {
        File[] snapshots = getSnapshots();
        File file = createTempFile("jpcovm");
        file.deleteOnExit();
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < snapshots.length; i++) {
                pw.println(snapshots[i].getAbsolutePath());
            }
            if (!isJProbe4Plus()) {
                pw.println(getProject().resolveFile(tofile.getPath()));
            }
            pw.flush();
        } catch (IOException e) {
            throw new BuildException("I/O error while writing to " + file, e);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ignored) {
                }
            }
        }
        return file;
    }

}
