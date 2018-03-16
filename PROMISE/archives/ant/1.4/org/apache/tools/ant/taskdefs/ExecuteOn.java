package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

import java.util.Hashtable;
import java.util.Vector;
import java.io.File;
import java.io.IOException;

/**
 * Executes a given command, supplying a set of files as arguments. 
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a> 
 */
public class ExecuteOn extends ExecTask {

    protected Vector filesets = new Vector();
    private boolean parallel = false;
    protected String type = "file";
    protected Commandline.Marker srcFilePos = null;
    private boolean skipEmpty = false;
    protected Commandline.Marker targetFilePos = null;
    protected Mapper mapperElement = null;
    protected FileNameMapper mapper = null;
    protected File destDir = null;

    /**
     * Has &lt;srcfile&gt; been specified before &lt;targetfile&gt;
     */
    protected boolean srcIsFirst = true;

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Shall the command work on all specified files in parallel?
     */
    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    /**
     * Shall the command work only on files, directories or both?
     */
    public void setType(FileDirBoth type) {
        this.type = type.getValue();
    }

    /**
     * Should empty filesets be ignored?
     */
    public void setSkipEmptyFilesets(boolean skip) {
        skipEmpty = skip;
    }

    /**
     * Set the destination directory.
     */
    public void setDest(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Marker that indicates where the name of the source file should
     * be put on the command line.
     */
    public Commandline.Marker createSrcfile() {
        if (srcFilePos != null) {
            throw new BuildException(taskType + " doesn\'t support multiple srcfile elements.",
                                     location);
        }
        srcFilePos = cmdl.createMarker();
        return srcFilePos;
    }

    /**
     * Marker that indicates where the name of the target file should
     * be put on the command line.
     */
    public Commandline.Marker createTargetfile() {
        if (targetFilePos != null) {
            throw new BuildException(taskType + " doesn\'t support multiple targetfile elements.",
                                     location);
        }
        targetFilePos = cmdl.createMarker();
        srcIsFirst = (srcFilePos != null);
        return targetFilePos;
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     */
    public Mapper createMapper() throws BuildException {
        if (mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper",
                                     location);
        }
        mapperElement = new Mapper(project);
        return mapperElement;
    }

    protected void checkConfiguration() {
        if ("execon".equals(taskName)) {
            log("!! execon is deprecated. Use apply instead. !!");
        }
        
        super.checkConfiguration();
        if (filesets.size() == 0) {
            throw new BuildException("no filesets specified", location);
        }

        if (targetFilePos != null || mapperElement != null 
            || destDir != null) {

            if (mapperElement == null) {
                throw new BuildException("no mapper specified", location);
            }
            if (mapperElement == null) {
                throw new BuildException("no dest attribute specified", 
                                         location);
            }
            mapper = mapperElement.getImplementation();
        }
    }

    protected void runExec(Execute exe) throws BuildException {
        try {

            Vector fileNames = new Vector();
            Vector baseDirs = new Vector();
            for (int i=0; i<filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                File base = fs.getDir(project);
                DirectoryScanner ds = fs.getDirectoryScanner(project);

                if (!"dir".equals(type)) {
                    String[] s = getFiles(base, ds);
                    for (int j=0; j<s.length; j++) {
                        fileNames.addElement(s[j]);
                        baseDirs.addElement(base);
                    }
                }

                if (!"file".equals(type)) {
                    String[] s = getDirs(base, ds);;
                    for (int j=0; j<s.length; j++) {
                        fileNames.addElement(s[j]);
                        baseDirs.addElement(base);
                    }
                }

                if (fileNames.size() == 0 && skipEmpty) {
                    log("Skipping fileset for directory "
                        + base + ". It is empty.", Project.MSG_INFO);
                    continue;
                }

                if (!parallel) {
                    String[] s = new String[fileNames.size()];
                    fileNames.copyInto(s);
                    for (int j=0; j<s.length; j++) {
                        String[] command = getCommandline(s[j], base);
                        log("Executing " + Commandline.toString(command), 
                            Project.MSG_VERBOSE);
                        exe.setCommandline(command);
                        runExecute(exe);
                    }
                    fileNames.removeAllElements();
                    baseDirs.removeAllElements();
                }
            }

            if (parallel) {
                String[] s = new String[fileNames.size()];
                fileNames.copyInto(s);
                File[] b = new File[baseDirs.size()];
                baseDirs.copyInto(b);
                String[] command = getCommandline(s, b);
                log("Executing " + Commandline.toString(command), 
                    Project.MSG_VERBOSE);
                exe.setCommandline(command);
                runExecute(exe);
            }

        } catch (IOException e) {
            throw new BuildException("Execute failed: " + e, e, location);
        } finally {
            logFlush();
        }
    }

    /**
     * Construct the command line for parallel execution.
     *
     * @param srcFiles The filenames to add to the commandline
     * @param baseDir filenames are relative to this dir
     */
    protected String[] getCommandline(String[] srcFiles, File[] baseDirs) {
        Vector targets = new Vector();
        if (targetFilePos != null) {
            Hashtable addedFiles = new Hashtable();
            for (int i=0; i<srcFiles.length; i++) {
                String[] subTargets = mapper.mapFileName(srcFiles[i]);
                if (subTargets != null) {
                    for (int j=0; j<subTargets.length; j++) {
                        String name = (new File(destDir, subTargets[j])).getAbsolutePath();
                        if (!addedFiles.contains(name)) {
                            targets.addElement(name);
                            addedFiles.put(name, name);
                        }
                    }
                }
            }
        }
        String[] targetFiles = new String[targets.size()];
        targets.copyInto(targetFiles);
        
        String[] orig = cmdl.getCommandline();
        String[] result = new String[orig.length+srcFiles.length+targetFiles.length];

        int srcIndex = orig.length;
        if (srcFilePos != null) {
            srcIndex = srcFilePos.getPosition();
        }

        if (targetFilePos != null) {
            int targetIndex = targetFilePos.getPosition();

            if (srcIndex < targetIndex 
                || (srcIndex == targetIndex && srcIsFirst)) {

                System.arraycopy(orig, 0, result, 0, srcIndex);
                
                System.arraycopy(orig, srcIndex, result, 
                                 srcIndex + srcFiles.length,
                                 targetIndex - srcIndex);
                
                System.arraycopy(targetFiles, 0, result, 
                                 targetIndex + srcFiles.length, 
                                 targetFiles.length);
                
                System.arraycopy(orig, targetIndex, result, 
                                 targetIndex + srcFiles.length + targetFiles.length,
                                 orig.length - targetIndex);
            } else {
                System.arraycopy(orig, 0, result, 0, targetIndex);
                
                System.arraycopy(targetFiles, 0, result, 
                                 targetIndex,
                                 targetFiles.length);
                
                System.arraycopy(orig, targetIndex, result, 
                                 targetIndex + targetFiles.length,
                                 srcIndex - targetIndex);
                
                System.arraycopy(orig, srcIndex, result, 
                                 srcIndex + srcFiles.length + targetFiles.length,
                                 orig.length - srcIndex);
                srcIndex += targetFiles.length;
            }


            System.arraycopy(orig, 0, result, 0, srcIndex);
            System.arraycopy(orig, srcIndex, result, 
                             srcIndex + srcFiles.length,
                             orig.length - srcIndex);

        }

        for (int i=0; i < srcFiles.length; i++) {
            result[srcIndex+i] = 
                (new File(baseDirs[i], srcFiles[i])).getAbsolutePath();
        }
        return result;
    }

    /**
     * Construct the command line for serial execution.
     *
     * @param srcFile The filename to add to the commandline
     * @param baseDir filename is relative to this dir
     */
    protected String[] getCommandline(String srcFile, File baseDir) {
        return getCommandline(new String[] {srcFile}, new File[] {baseDir});
    }

    /**
     * Return the list of files from this DirectoryScanner that should
     * be included on the command line.
     */
    protected String[] getFiles(File baseDir, DirectoryScanner ds) {
        if (mapper != null) {
            SourceFileScanner sfs = new SourceFileScanner(this);
            return sfs.restrict(ds.getIncludedFiles(), baseDir, destDir, 
                                mapper);
        } else {
            return ds.getIncludedFiles();
        }
    }

    /**
     * Return the list of Directories from this DirectoryScanner that
     * should be included on the command line.
     */
    protected String[] getDirs(File baseDir, DirectoryScanner ds) {
        if (mapper != null) {
            SourceFileScanner sfs = new SourceFileScanner(this);
            return sfs.restrict(ds.getIncludedDirectories(), baseDir, destDir, 
                                mapper);
        } else {
            return ds.getIncludedDirectories();
        }
    }

    /**
     * Enumerated attribute with the values "file", "dir" and "both"
     * for the type attribute.  
     */
    public static class FileDirBoth extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[] {"file", "dir", "both"};
        }
    }
}
