package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Task to convert text source files to local OS formatting conventions, as
 * well as repair text files damaged by misconfigured or misguided editors or
 * file transfer programs.
 * <p>
 * This task can take the following arguments:
 * <ul>
 * <li>srcdir
 * <li>destdir
 * <li>include
 * <li>exclude
 * <li>cr
 * <li>tab
 * <li>eof
 * </ul>
 * Of these arguments, only <b>sourcedir</b> is required.
 * <p>
 * When this task executes, it will scan the srcdir based on the include
 * and exclude properties.
 * <p>
 * <em>Warning:</em> do not run on binary or carefully formatted files.
 * this may sound obvious, but if you don't specify asis, presume that
 * your files are going to be modified.  If you want tabs to be fixed,
 * whitespace characters may be added or removed as necessary.  Similarly,
 * for CR's - in fact cr="add" can result in cr characters being removed.
 * (to handle cases where other programs have converted CRLF into CRCRLF).
 *
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 */

public class FixCRLF extends MatchingTask {


    private File srcDir;
    private File destDir = null;

    /**
     * Defaults the properties based on the system type.
     * <ul><li>Unix: cr="remove" tab="asis" eof="remove"
     *     <li>DOS: cr="add" tab="asis" eof="asis"</ul>
     */
    public FixCRLF() {
        if (System.getProperty("path.separator").equals(":")) {
        } else {
        }
    }

    /**
     * Set the source dir to find the source text files.
     */
    public void setSrcdir(File srcDir) {
        this.srcDir = srcDir;
    }

    /**
     * Set the destination where the fixed files should be placed.
     * Default is to replace the original file.
     */
    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Specify how carriage return (CR) charaters are to be handled
     *
     * @param option valid values:
     * <ul>
     * <li>add: ensure that there is a CR before every LF
     * <li>asis: leave CR characters alone
     * <li>remove: remove all CR characters
     * </ul>
     */
    public void setCr(AddAsisRemove attr) {
        String option = attr.getValue();
        if (option.equals("remove")) {
            addcr = -1;
        } else if (option.equals("asis")) {
            addcr = 0;
        } else {
            addcr = +1;
        }
    }

    /**
     * Specify how tab charaters are to be handled
     *
     * @param option valid values:
     * <ul>
     * <li>add: convert sequences of spaces which span a tab stop to tabs
     * <li>asis: leave tab and space characters alone
     * <li>remove: convert tabs to spaces
     * </ul>
     */
    public void setTab(AddAsisRemove attr) {
        String option = attr.getValue();
        if (option.equals("remove")) {
            addtab = -1;
        } else if (option.equals("asis")) {
            addtab = 0;
        } else {
            addtab = +1;
        }
    }

    /**
     * Specify tab length in characters
     *
     * @param tlength specify the length of tab in spaces, has to be a power of 2
     */
    public void setTablength(int tlength) throws BuildException {
        if (tlength < 2 || (tlength & (tlength-1)) != 0) {
            throw new BuildException("tablength must be a positive power of 2",
                                     location);
        }
        tablength = tlength;
    }

    /**
     * Specify how DOS EOF (control-z) charaters are to be handled
     *
     * @param option valid values:
     * <ul>
     * <li>add: ensure that there is an eof at the end of the file
     * <li>asis: leave eof characters alone
     * <li>remove: remove any eof character found at the end
     * </ul>
     */
    public void setEof(AddAsisRemove attr) {
        String option = attr.getValue();
        if (option.equals("remove")) {
            ctrlz = -1;
        } else if (option.equals("asis")) {
            ctrlz = 0;
        } else {
            ctrlz = +1;
        }
    }

    /**
     * Executes the task.
     */
    public void execute() throws BuildException {

        if (srcDir == null) {
            throw new BuildException("srcdir attribute must be set!");
        }
        if (!srcDir.exists()) {
            throw new BuildException("srcdir does not exist!");
        }
        if (!srcDir.isDirectory()) {
            throw new BuildException("srcdir is not a directory!");
        }
        if (destDir != null) {
            if (!destDir.exists()) {
                throw new BuildException("destdir does not exist!");
            }
            if (!destDir.isDirectory()) {
                throw new BuildException("destdir is not a directory!");
            }
        }

        log("options:" +
            " cr=" + (addcr==-1 ? "add" : addcr==0 ? "asis" : "remove") +
            " tab=" + (addtab==-1 ? "add" : addtab==0 ? "asis" : "remove") +
            " eof=" + (ctrlz==-1 ? "add" : ctrlz==0 ? "asis" : "remove") +
            " tablength=" + tablength,
            Project.MSG_VERBOSE);

        DirectoryScanner ds = super.getDirectoryScanner(srcDir);
        String[] files = ds.getIncludedFiles();

        for (int i = 0; i < files.length; i++) {
            File srcFile = new File(srcDir, files[i]);

            int count = (int)srcFile.length();
            byte indata[] = new byte[count];
            try {
                FileInputStream inStream = new FileInputStream(srcFile);
                inStream.read(indata);
                inStream.close();
            } catch (IOException e) {
                throw new BuildException(e);
            }

            int cr = 0;
            int lf = 0;
            int tab = 0;

            for (int k=0; k<count; k++) {
                byte c = indata[k];
                if (c == '\r') cr++;
                if (c == '\n') lf++;
                if (c == '\t') tab++;
            }

            boolean eof = ((count>0) && (indata[count-1] == 0x1A));

            log(srcFile + ": size=" + count + " cr=" + cr +
                        " lf=" + lf + " tab=" + tab + " eof=" + eof,
                        Project.MSG_VERBOSE);

            int outsize = count;
            if (addcr  !=  0) outsize-=cr;
            if (addcr  == +1) outsize+=lf;
            if (addtab == -1) outsize+=tab*(tablength-1);
            if (ctrlz  == +1) outsize+=1;

            byte outdata[] = new byte[outsize];

            for (int k=0; k<count; k++) {
                switch (indata[k]) {
                    case (byte)' ':
                        if (addtab == 0) outdata[o++]=(byte)' ';
                        col++;
                        break;

                    case (byte)'\t':
                        if (addtab == 0) {
                            outdata[o++]=(byte)'\t';
                            col++;
                        } else {
                            col = (col|(tablength-1))+1;
                        }
                        break;

                    case (byte)'\r':
                        if (addcr == 0) {
                            outdata[o++]=(byte)'\r';
                            col++;
                        }
                        break;

                    case (byte)'\n':
                        if (addcr == +1) outdata[o++]=(byte)'\r';
                        outdata[o++]=(byte)'\n';
                        line=o;
                        col=0;
                        break;

                    default:
                        if (addtab>0 && o+1<line+col) {
                            int diff=o-line;

                            while ((diff|(tablength-1))<col) {
                                outdata[o++]=(byte)'\t';
                                line-=(tablength-1)-(diff&(tablength-1));
                                diff=o-line;
                            };
                        };

                        while (o<line+col) outdata[o++]=(byte)' ';

                        outdata[o++]=indata[k];
                        col++;
                }
            }

            if (ctrlz == +1) {
                if (outdata[o-1]!=0x1A) outdata[o++]=0x1A;
            } else if (ctrlz == -1) {
                if (o>2 && outdata[o-1]==0x0A && outdata[o-2]==0x1A) o--;
                if (o>1 && outdata[o-1]==0x1A) o--;
            }

            try {
                File destFile = srcFile;
                if (destDir != null) destFile = new File(destDir, files[i]);
                FileOutputStream outStream = new FileOutputStream(destFile);
                outStream.write(outdata,0,o);
                outStream.close();
            } catch (IOException e) {
                throw new BuildException(e);
            }

        } /* end for */
    }

    /**
     * Enumerated attribute with the values "asis", "add" and "remove".
     */
    public static class AddAsisRemove extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[] {"add", "asis", "remove"};
        }
    }
}
