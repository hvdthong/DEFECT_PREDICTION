package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Date;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FileList;

/**
 * A Task to record explicit dependencies.  If any of the target files
 * are out of date with respect to any of the source files, all target
 * files are removed.  This is useful where dependencies cannot be
 * computed (for example, dynamically interpreted parameters or files
 * that need to stay in synch but are not directly linked) or where
 * the ant task in question could compute them but does not (for
 * example, the linked DTD for an XML file using the style task).
 *
 * nested arguments:
 * <ul>
 * <li>srcfileset     (fileset describing the source files to examine)
 * <li>srcfilelist    (filelist describing the source files to examine)
 * <li>targetfileset  (fileset describing the target files to examine)
 * <li>targetfilelist (filelist describing the target files to examine)
 * </ul>
 * At least one instance of either a fileset or filelist for both source and target
 * are required.
 * <p>
 * This task will examine each of the source files against each of the target files.
 * If any target files are out of date with respect to any of the source files, all targets are removed.
 * If any files named in a (src or target) filelist do not exist, all targets are removed.
 * Hint: If missing files should be ignored, specify them as include patterns in filesets,
 * rather than using filelists.
 * </p><p>
 * This task attempts to optimize speed of dependency checking.  It will stop after the first
 * out of date file is found and remove all targets, rather than exhaustively checking every
 * source vs target combination unnecessarily.
 * </p><p>
 * Example uses: 
 * <ulist><li>
 * Record the fact that an XML file must be up to date
 * with respect to its XSD (Schema file), even though the XML file
 * itself includes no reference to its XSD.
 * </li><li>
 * Record the fact that an XSL stylesheet includes other
 * sub-stylesheets
 * </li><li>
 * Record the fact that java files must be recompiled if the ant build
 * file changes
 * </li></ulist>
 * 
 * @author <a href="mailto:cstrong@arielpartners.com">Craeg Strong</a>
 * @version $Revision: 269585 $ $Date: 2001-08-18 22:22:09 +0800 (周六, 2001-08-18) $
 */
public class DependSet extends MatchingTask {

    private Vector sourceFileSets  = new Vector();
    private Vector sourceFileLists = new Vector();
    private Vector targetFileSets  = new Vector();
    private Vector targetFileLists = new Vector();

    /**
     * Creates a new DependSet Task.
     **/
    public DependSet() {

    /**
     * Nested &lt;srcfileset&gt; element.
     */
    public void addSrcfileset(FileSet fs) {
        sourceFileSets.addElement(fs);
    }

    /**
     * Nested &lt;srcfilelist&gt; element.
     */
    public void addSrcfilelist(FileList fl) {
        sourceFileLists.addElement(fl);
    }

    /**
     * Nested &lt;targetfileset&gt; element.
     */
    public void addTargetfileset(FileSet fs) {
        targetFileSets.addElement(fs);
    }

    /**
     * Nested &lt;targetfilelist&gt; element.
     */
    public void addTargetfilelist(FileList fl) {
        targetFileLists.addElement(fl);
    }

    /**
     * Executes the task.
     */

    public void execute() throws BuildException {

        if ( (sourceFileSets.size() == 0) && (sourceFileLists.size() == 0) ) { 
          throw new BuildException("At least one <srcfileset> or <srcfilelist> element must be set");
        }

        if ( (targetFileSets.size() == 0) && (targetFileLists.size() == 0) ) {
          throw new BuildException("At least one <targetfileset> or <targetfilelist> element must be set");
        }

        long now = (new Date()).getTime();
        /*
          If we're on Windows, we have to munge the time up to 2 secs to
          be able to check file modification times.
          (Windows has a max resolution of two secs for modification times)
        */
        String osname = System.getProperty("os.name").toLowerCase();
        if ( osname.indexOf("windows") >= 0 ) {
            now += 2000;
        }

        Vector  allTargets         = new Vector();
        Enumeration enumTargetSets = targetFileSets.elements();
        while (enumTargetSets.hasMoreElements()) {
                 
           FileSet targetFS          = (FileSet) enumTargetSets.nextElement();
           DirectoryScanner targetDS = targetFS.getDirectoryScanner(project);
           String[] targetFiles      = targetDS.getIncludedFiles();
                 
           for (int i = 0; i < targetFiles.length; i++) {
                    
              File dest = new File(targetFS.getDir(project), targetFiles[i]);
              allTargets.addElement(dest);

              if (dest.lastModified() > now) {
                 log("Warning: "+targetFiles[i]+" modified in the future.", 
                     Project.MSG_WARN);
              }
           }
        }

        boolean upToDate            = true;
        Enumeration enumTargetLists = targetFileLists.elements();
        while (enumTargetLists.hasMoreElements()) {
                 
           FileList targetFL    = (FileList) enumTargetLists.nextElement();
           String[] targetFiles = targetFL.getFiles(project);
                 
           for (int i = 0; i < targetFiles.length; i++) {
                    
              File dest = new File(targetFL.getDir(project), targetFiles[i]);
              if (!dest.exists()) {
                 log(targetFiles[i]+ " does not exist.", Project.MSG_VERBOSE);
                 upToDate = false;
                 continue;
              }
              else {
                 allTargets.addElement(dest);
              }
              if (dest.lastModified() > now) {
                 log("Warning: "+targetFiles[i]+" modified in the future.", 
                     Project.MSG_WARN);
              }
           }
        }

        if (upToDate) {
           Enumeration enumSourceSets = sourceFileSets.elements();
           while (upToDate && enumSourceSets.hasMoreElements()) {
          
              FileSet sourceFS          = (FileSet) enumSourceSets.nextElement();
              DirectoryScanner sourceDS = sourceFS.getDirectoryScanner(project);
              String[] sourceFiles      = sourceDS.getIncludedFiles();

              int i = 0;
              do {
                 File src = new File(sourceFS.getDir(project), sourceFiles[i]);

                 if (src.lastModified() > now) {
                    log("Warning: "+sourceFiles[i]+" modified in the future.", 
                        Project.MSG_WARN);
                 }

                 Enumeration enumTargets = allTargets.elements();
                 while (upToDate && enumTargets.hasMoreElements()) {
                 
                    File dest = (File)enumTargets.nextElement();
                    if (src.lastModified() > dest.lastModified()) {
                       log(dest.getPath() + " is out of date with respect to " +
                                sourceFiles[i], Project.MSG_VERBOSE);
                       upToDate = false;

                    }
                 }
              } while (upToDate && (++i < sourceFiles.length) );
           }
        }

        if (upToDate) {
           Enumeration enumSourceLists = sourceFileLists.elements();
           while (upToDate && enumSourceLists.hasMoreElements()) {
          
              FileList sourceFL         = (FileList) enumSourceLists.nextElement();
              String[] sourceFiles      = sourceFL.getFiles(project);

              int i = 0;
              do {
                 File src = new File(sourceFL.getDir(project), sourceFiles[i]);

                 if (src.lastModified() > now) {
                    log("Warning: "+sourceFiles[i]+" modified in the future.", 
                        Project.MSG_WARN);
                 }

                 if (!src.exists()) {
                    log(sourceFiles[i]+ " does not exist.", Project.MSG_VERBOSE);
                    upToDate = false;
                    break;
                 }

                 Enumeration enumTargets = allTargets.elements();
                 while (upToDate && enumTargets.hasMoreElements()) {
                 
                    File dest = (File)enumTargets.nextElement();
                    
                    if (src.lastModified() > dest.lastModified()) {
                       log(dest.getPath() + " is out of date with respect to " +
                                sourceFiles[i], Project.MSG_VERBOSE);
                       upToDate = false;

                    }
                 }
              } while (upToDate && (++i < sourceFiles.length) );
           }
        }

        if (!upToDate) {
           log("Deleting all target files. ", Project.MSG_VERBOSE);
           for (Enumeration e = allTargets.elements(); e.hasMoreElements(); ) {
              File fileToRemove = (File)e.nextElement();
              log("Deleting file " + fileToRemove.getAbsolutePath(), Project.MSG_VERBOSE);
              fileToRemove.delete();
           }
        }
        

   
