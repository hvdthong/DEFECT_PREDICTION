package org.apache.tools.ant.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;

import java.io.File;
import java.util.Vector;

/**
 * this class provides utility methods to process resources
 *
 * @author <a href="mailto:levylambert@tiscali-dsl.de">Antoine Levy-Lambert</a>
 * @since Ant 1.5.2
 */
public class ResourceUtils {

    /**                                                                      {
     * tells which source files should be reprocessed based on the
     * last modification date of target files
     * @param logTo where to send (more or less) interesting output
     * @param source array of resources bearing relative path and last
     * modification date
     * @param mapper filename mapper indicating how to find the target
     * files
     * @param targets object able to map as a resource a relative path
     * at <b>destination</b>
     * @return array containing the source files which need to be
     * copied or processed, because the targets are out of date or do
     * not exist
     */
    public static Resource[] selectOutOfDateSources(ProjectComponent logTo,
                                                    Resource[] source,
                                                    FileNameMapper mapper,
                                                    ResourceFactory targets) {
        long now = (new java.util.Date()).getTime();
        StringBuffer targetList = new StringBuffer();

        /*
          If we're on Windows, we have to munge the time up to 2 secs to
          be able to check file modification times.
          (Windows has a max resolution of two secs for modification times)
          Actually this is a feature of the FAT file system, NTFS does
          not have it, so if we could reliably passively test for an NTFS
          file systems we could turn this off...
        */
        if (Os.isFamily("windows")) {
            now += 2000;
        }

        Vector vresult = new Vector();
        for (int counter = 0; counter < source.length; counter++) {
            if (source[counter].getLastModified() > now) {
                logTo.log("Warning: " + source[counter].getName() 
                         + " modified in the future.", 
                         Project.MSG_WARN);
            }

            String[] targetnames = 
                mapper.mapFileName(source[counter].getName()
                                   .replace('/', File.separatorChar));
            if (targetnames != null) {
                boolean added = false;
                targetList.setLength(0);
                for (int ctarget = 0; !added && ctarget < targetnames.length; 
                     ctarget++) {
                    Resource atarget = 
                        targets.getResource(targetnames[ctarget]
                                            .replace(File.separatorChar, '/'));
                    if (!atarget.isExists()) {
                        logTo.log(source[counter].getName() + " added as " 
                                  + atarget.getName()
                                  + " doesn\'t exist.", Project.MSG_VERBOSE);
                        vresult.addElement(source[counter]);
                        added = true;
                    } else if (atarget.getLastModified() 
                               < source[counter].getLastModified()) {
                        logTo.log(source[counter].getName() + " added as " 
                                  + atarget.getName()
                                  + " is outdated.", Project.MSG_VERBOSE);
                        vresult.addElement(source[counter]);
                        added = true;
                    } else {
                        if (targetList.length() > 0) {
                            targetList.append(", ");
                        }
                        targetList.append(atarget.getName());
                    }
                }

                if (!added) {
                    logTo.log(source[counter].getName() 
                              + " omitted as " + targetList.toString()
                              + (targetnames.length == 1 ? " is" : " are ")
                              + " up to date.", Project.MSG_VERBOSE);
                }
            } else {
                logTo.log(source[counter].getName() 
                          + " skipped - don\'t know how to handle it",
                          Project.MSG_VERBOSE);
            }
        }
        Resource[] result= new Resource[vresult.size()];
        vresult.copyInto(result);
        return result;
    }
}
