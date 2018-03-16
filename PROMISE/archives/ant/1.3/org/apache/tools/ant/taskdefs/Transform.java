package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

/**
 * Executes a given command, supplying a set of files as arguments. 
 *
 * <p>Only those files that are newer than their corresponding target
 * files will be handeled, the rest will be ignored.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class Transform extends ExecuteOn {

    protected Commandline.Marker targetFilePos = null;
    protected Mapper mapperElement = null;
    protected FileNameMapper mapper = null;
    protected File destDir = null;

    /**
     * Has &lt;srcfile&gt; been specified before &lt;targetfile&gt;
     */
    protected boolean srcIsFirst = true;

    /**
     * Set the destination directory.
     */
    public void setDest(File destDir) {
        this.destDir = destDir;
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
        super.checkConfiguration();
        if (mapperElement == null) {
            throw new BuildException("no mapper specified", location);
        }
        if (destDir == null) {
            throw new BuildException("no dest attribute specified", location);
        }

        mapper = mapperElement.getImplementation();
    }

    /**
     * Return the list of files from this DirectoryScanner that should
     * be included on the command line - i.e. only those that are
     * newer than the corresponding target files.
     */
    protected String[] getFiles(File baseDir, DirectoryScanner ds) {
        SourceFileScanner sfs = new SourceFileScanner(this);
        return sfs.restrict(ds.getIncludedFiles(), baseDir, destDir, mapper);
    }

    /**
     * Return the list of Directories from this DirectoryScanner that
     * should be included on the command line - i.e. only those that
     * are newer than the corresponding target files.
     */
    protected String[] getDirs(File baseDir, DirectoryScanner ds) {
        SourceFileScanner sfs = new SourceFileScanner(this);
        return sfs.restrict(ds.getIncludedDirectories(), baseDir, destDir, 
                            mapper);
    }

    /**
     * Construct the command line for parallel execution.
     *
     * @param srcFiles The filenames to add to the commandline
     * @param baseDir filenames are relative to this dir
     */
    protected String[] getCommandline(String[] srcFiles, File baseDir) {
        if (targetFilePos == null) {
            return super.getCommandline(srcFiles, baseDir);
        }

        Vector targets = new Vector();
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
        String[] targetFiles = new String[targets.size()];
        targets.copyInto(targetFiles);
        
        String[] orig = cmdl.getCommandline();
        String[] result = new String[orig.length+srcFiles.length+targetFiles.length];

        int srcIndex = orig.length;
        if (srcFilePos != null) {
            srcIndex = srcFilePos.getPosition();
        }
        int targetIndex = targetFilePos.getPosition();

        if (srcIndex < targetIndex || (srcIndex == targetIndex && srcIsFirst)) {
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


        for (int i=0; i < srcFiles.length; i++) {
            result[srcIndex+i] = 
                (new File(baseDir, srcFiles[i])).getAbsolutePath();
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
        return getCommandline(new String[] {srcFile}, baseDir);
    }

}
