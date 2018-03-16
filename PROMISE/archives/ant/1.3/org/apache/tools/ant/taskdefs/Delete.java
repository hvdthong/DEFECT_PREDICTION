package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.io.*;
import java.util.*;

/**
 * Deletes a file or directory, or set of files defined by a fileset.
 * The original delete task would delete a file, or a set of files 
 * using the include/exclude syntax.  The deltree task would delete a 
 * directory tree.  This task combines the functionality of these two
 * originally distinct tasks.
 * <p>Currently Delete extends MatchingTask.  This is intend <i>only</i>
 * to provide backwards compatibility for a release.  The future position
 * is to use nested filesets exclusively.</p>
 * 
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Tom Dimock <a href="mailto:tad1@cornell.edu">tad1@cornell.edu</a>
 * @author Glenn McAllister <a href="mailto:glennm@ca.ibm.com">glennm@ca.ibm.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@latchkey.com">jon@latchkey.com</a>
 */
public class Delete extends MatchingTask {
    protected File file = null;
    protected File dir = null;
    protected Vector filesets = new Vector();
    protected boolean usedMatchingTask = false;

    private int verbosity = Project.MSG_VERBOSE;
    private boolean quiet = false;

    /**
     * Set the name of a single file to be removed.
     * 
     * @param file the file to be deleted
     */
    public void setFile(File file) {
        this.file = file;
    } 

    /**
     * Set the directory from which files are to be deleted
     * 
     * @param dir the directory path.
     */
    public void setDir(File dir) {
        this.dir = dir;
    } 

    /**
     * Used to force listing of all names of deleted files.
     * 
     * @param verbose "true" or "on"
     */
    public void setVerbose(boolean verbose) {
        if (verbose) {
            this.verbosity = Project.MSG_INFO;
        } else {
            this.verbosity = Project.MSG_VERBOSE;
        } 
    } 

    /**
     * If the file does not exist, do not display a diagnostic 
     * message or modify the exit status to reflect an error.
     * This means that if a file or directory cannot be deleted,
     * then no error is reported. This setting emulates the 
     * -f option to the Unix &quot;rm&quot; command.
     * Default is false meaning things are &quot;noisy&quot;
     * @param quiet "true" or "on"
     */
    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    } 

    /**
     * Used to delete empty directories.
     */
    public void setIncludeEmptyDirs(boolean includeEmpty) {
        this.includeEmpty = includeEmpty;
    }

   /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }
 
    /**
     * add a name entry on the include list
     */
    public PatternSet.NameEntry createInclude() {
        usedMatchingTask = true;
        return super.createInclude();
    }
    
    /**
     * add a name entry on the exclude list
     */
    public PatternSet.NameEntry createExclude() {
        usedMatchingTask = true;
        return super.createExclude();
    }

    /**
     * add a set of patterns
     */
    public PatternSet createPatternSet() {
        usedMatchingTask = true;
        return super.createPatternSet();
    }

    /**
     * Sets the set of include patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes(String includes) {
        usedMatchingTask = true;
        super.setIncludes(includes);
    }

    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes(String excludes) {
        usedMatchingTask = true;
        super.setExcludes(excludes);
    }

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions 
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
        usedMatchingTask = true;
        super.setDefaultexcludes(useDefaultExcludes);
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param includesfile A string containing the filename to fetch
     * the include patterns from.  
     */
    public void setIncludesfile(File includesfile) {
        usedMatchingTask = true;
        super.setIncludesfile(includesfile);
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param excludesfile A string containing the filename to fetch
     * the include patterns from.  
     */
    public void setExcludesfile(File excludesfile) {
        usedMatchingTask = true;
        super.setExcludesfile(excludesfile);
    }

    /**
     * Delete the file(s).
     */
    public void execute() throws BuildException {
        if (usedMatchingTask) {
            log("DEPRECATED - Use of the implicit FileSet is deprecated.  Use a nested fileset element instead.");
        }

        if (file == null && dir == null && filesets.size() == 0) {
            throw new BuildException("At least one of the file or dir attributes, or a fileset element, must be set.");
        } 

        if (file != null) {
            if (file.exists()) {
                if (file.isDirectory()) {
                    log("Directory " + file.getAbsolutePath() + " cannot be removed using the file attribute.  Use dir instead.");
                } else {
                    log("Deleting: " + file.getAbsolutePath());
  
                    if (!file.delete() && !quiet) {
                        throw new BuildException("Unable to delete file " + file.getAbsolutePath());
                    } 
                } 
            } else {
                log("Could not find file " + file.getAbsolutePath() + " to delete.");
            }
        }

        if (dir != null && dir.exists() && dir.isDirectory() && !usedMatchingTask) {
            /*
               If verbosity is MSG_VERBOSE, that mean we are doing regular logging
               (backwards as that sounds).  In that case, we want to print one 
               message about deleting the top of the directory tree.  Otherwise, 
               the removeDir method will handle messages for _all_ directories.
             */
            if (verbosity == Project.MSG_VERBOSE) {
                log("Deleting directory " + dir.getAbsolutePath());
            }
            removeDir(dir);
        }

        for (int i=0; i<filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(project);
            String[] files = ds.getIncludedFiles();
            String[] dirs = ds.getIncludedDirectories();
            removeFiles(fs.getDir(project), files, dirs);
        }

        if (usedMatchingTask && dir != null) {
            DirectoryScanner ds = super.getDirectoryScanner(dir);
            String[] files = ds.getIncludedFiles();
            String[] dirs = ds.getIncludedDirectories();
            removeFiles(dir, files, dirs);
        }
    } 


    protected void removeDir(File d) {
        String[] list = d.list();
        if (list == null) list = new String[0];
        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(d, s);
            if (f.isDirectory()) {
                removeDir(f);
            } else {
                log("Deleting " + f.getAbsolutePath(), verbosity);
                if (!f.delete() && !quiet) {
                    throw new BuildException("Unable to delete file " + f.getAbsolutePath());
                }
            }
        }
        log("Deleting directory " + d.getAbsolutePath(), verbosity);
        if (!d.delete() && !quiet) {
            throw new BuildException("Unable to delete directory " + dir.getAbsolutePath());
        }
    }

    protected void removeFiles(File d, String[] files, String[] dirs) {
        if (files.length > 0) {
            log("Deleting " + files.length + " files from " + d.getAbsolutePath());
            for (int j=0; j<files.length; j++) {
                File f = new File(d, files[j]);
                log("Deleting " + f.getAbsolutePath(), verbosity);
                if (!f.delete() && !quiet) {
                    throw new BuildException("Unable to delete file " + f.getAbsolutePath());
                }
            }
        }

        if (dirs.length > 0 && includeEmpty) {
            int dirCount = 0;
            for (int j=0; j<dirs.length; j++) {
                File dir = new File(d, dirs[j]);
                String[] dirFiles = dir.list();
                if (dirFiles == null || dirFiles.length == 0) {
                    log("Deleting " + dir.getAbsolutePath(), verbosity);
                    if (!dir.delete() && !quiet) {
                        throw new BuildException("Unable to delete directory " + dir.getAbsolutePath());
                    } else {
                        dirCount++;
                    }
                }
            }

            if (dirCount > 0) {
                log("Deleted " + dirCount + " director" + 
                    (dirCount==1 ? "y" : "ies") + 
                    " from " + d.getAbsolutePath());
            }
        }
    }
}

