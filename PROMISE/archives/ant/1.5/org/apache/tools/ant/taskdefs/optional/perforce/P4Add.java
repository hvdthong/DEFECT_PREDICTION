package org.apache.tools.ant.taskdefs.optional.perforce;


import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/** Adds specified files to Perforce.
 *
 * <b>Example Usage:</b>
 * <table border="1">
 * <th>Function</th><th>Command</th>
 * <tr><td>Specify the length of command line arguments to pass to each invocation of p4</td><td>&lt;p4add Commandlength="450"&gt;</td></tr>
 * </table>
 *
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 * @author <A HREF="mailto:ashundi@tibco.com">Anli Shundi</A>
 */
public class P4Add extends P4Base {

    private int changelist;
    private String addCmd = "";
    private Vector filesets = new Vector();
    private int cmdLength = 450;

    /**
     *   positive integer specifying the maximum length
     *   of the commandline when calling Perforce to add the files. 
     *   Defaults to 450, higher values mean faster execution,
     *   but also possible failures.
     */
     
    public void setCommandlength(int len) throws BuildException {
        if (len <= 0) {
            throw new BuildException("P4Add: Commandlength should be a positive number");
        }
        this.cmdLength = len;
    }

    /**
     * If specified the open files are associated with the
     * specified pending changelist number; otherwise the open files are
     * associated with the default changelist.
     */
    public void setChangelist(int changelist) throws BuildException {
        if (changelist <= 0) {
            throw new BuildException("P4Add: Changelist# should be a positive number");
        }

        this.changelist = changelist;
    }

    /**
     * files to add
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    public void execute() throws BuildException {

        if (P4View != null) {
            addCmd = P4View;
        }

        P4CmdOpts = (changelist > 0) ? ("-c " + changelist) : "";

        StringBuffer filelist = new StringBuffer();

        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(project);

            String[] srcFiles = ds.getIncludedFiles();
            if (srcFiles != null) {
                for (int j = 0; j < srcFiles.length; j++) {
                    File f = new File(ds.getBasedir(), srcFiles[j]);
                    filelist.append(" ").append('"').append(f.getAbsolutePath()).append('"');
                    if (filelist.length() > cmdLength) {
                        execP4Add(filelist);
                        filelist.setLength(0);
                    }
                }
                if (filelist.length() > 0) {
                    execP4Add(filelist);
                }
            } else {
                log("No files specified to add!", Project.MSG_WARN);
            }
        }

    }

    private void execP4Add(StringBuffer list) {
        log("Execing add " + P4CmdOpts + " " + addCmd + list, Project.MSG_INFO);

        execP4Command("-s add " + P4CmdOpts + " " + addCmd + list, new SimpleP4OutputHandler(this));
    }
}
