package org.apache.tools.ant.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.io.File;
import java.util.Vector;

/**
 * Utility class that collects the functionality of the various
 * scanDir methods that have been scattered in several tasks before.
 *
 * <p>The only method returns an array of source files. The array is a
 * subset of the files given as a parameter and holds only those that
 * are newer than their corresponding target files.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class SourceFileScanner {

    protected Task task;

    /**
     * @param task The task we should log messages through
     */
    public SourceFileScanner(Task task) {
        this.task = task;
    }

    /**
     * Restrict the given set of files to those that are newer than
     * their corresponding target files.
     *
     * @param files   the original set of files
     * @param srcDir  all files are relative to this directory
     * @param destDir target files live here. if null file names
     *                returned by the mapper are assumed to be absolute.
     * @param mapper  knows how to construct a target file names from
     *                source file names.
     */
    public String[] restrict(String[] files, File srcDir, File destDir,
                             FileNameMapper mapper) {

        long now = (new java.util.Date()).getTime();
        StringBuffer targetList = new StringBuffer();

        /*
          If we're on Windows, we have to munge the time up to 2 secs to
          be able to check file modification times.
          (Windows has a max resolution of two secs for modification times)
        */
        String osname = System.getProperty("os.name").toLowerCase();
        if ( osname.indexOf("windows") >= 0 ) {
            now += 2000;
        }

        Vector v = new Vector();
        for (int i=0; i< files.length; i++) {

            String[] targets = mapper.mapFileName(files[i]);
            if (targets == null || targets.length == 0) {
                task.log(files[i]+" skipped - don\'t know how to handle it",
                         Project.MSG_VERBOSE);
                continue;
            }

            File src = new File(srcDir, files[i]);
            if (src.lastModified() > now) {
                task.log("Warning: "+files[i]+" modified in the future.", 
                         Project.MSG_WARN);
            }

            boolean added = false;
            targetList.setLength(0);
            for (int j=0; !added && j<targets.length; j++) {
                File dest = null;
                if (destDir == null) {
                    dest = new File(targets[j]);
                } else {
                    dest = new File(destDir, targets[j]);
                }
                
                if (!dest.exists()) {
                    task.log(files[i]+" added as "+dest.getAbsolutePath()+" doesn\'t exist.",
                             Project.MSG_VERBOSE);
                    v.addElement(files[i]);
                    added = true;
                } else if (src.lastModified() > dest.lastModified()) {
                    task.log(files[i]+" added as "+dest.getAbsolutePath()+" is outdated.",
                             Project.MSG_VERBOSE);
                    v.addElement(files[i]);
                    added = true;
                } else {
                    if (targetList.length() > 0) {
                        targetList.append(", ");
                    }
                    targetList.append(dest.getAbsolutePath());
                }
            }

            if (!added) {
                task.log(files[i]+" omitted as "+targetList.toString()
                         + (targets.length == 1 ? " is" : " are ")
                         + " up to date.", Project.MSG_VERBOSE);
            }
            
        }
        String[] result = new String[v.size()];
        v.copyInto(result);
        return result;
    }

    /**
     * Convinience layer on top of restrict that returns the source
     * files as File objects (containing absolute paths if srcDir is
     * absolute).
     */
    public File[] restrictAsFiles(String[] files, File srcDir, File destDir,
                                  FileNameMapper mapper) {
        String[] res = restrict(files, srcDir, destDir, mapper);
        File[] result = new File[res.length];
        for (int i=0; i<res.length; i++) {
            result[i] = new File(srcDir, res[i]);
        }
        return result;
    }
}
