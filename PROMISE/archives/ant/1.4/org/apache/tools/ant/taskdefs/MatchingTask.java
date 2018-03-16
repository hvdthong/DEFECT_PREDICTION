package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import java.io.*;
import java.util.*;

/**
 * This is an abstract task that should be used by all those tasks that 
 * require to include or exclude files based on pattern matching.
 *
 * @author Arnout J. Kuiper <a href="mailto:ajkuiper@wxs.nl">ajkuiper@wxs.nl</a> 
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public abstract class MatchingTask extends Task {

    protected boolean useDefaultExcludes = true;
    protected FileSet fileset = new FileSet();

    /**
     * add a name entry on the include list
     */
    public PatternSet.NameEntry createInclude() {
        return fileset.createInclude();
    }
    
    /**
     * add a name entry on the include files list
     */
    public PatternSet.NameEntry createIncludesFile() {
        return fileset.createIncludesFile();
    }
    
    /**
     * add a name entry on the exclude list
     */
    public PatternSet.NameEntry createExclude() {
        return fileset.createExclude();
    }

    /**
     * add a name entry on the include files list
     */
    public PatternSet.NameEntry createExcludesFile() {
        return fileset.createExcludesFile();
    }
    
    /**
     * add a set of patterns
     */
    public PatternSet createPatternSet() {
        return fileset.createPatternSet();
    }

    /**
     * Sets the set of include patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes(String includes) {
        fileset.setIncludes(includes);
    }

    /**
     * Set this to be the items in the base directory that you want to be
     * included. You can also specify "*" for the items (ie: items="*") 
     * and it will include all the items in the base directory.
     *
     * @param itemString the string containing the files to include.
     */
    public void XsetItems(String itemString) {
        log("The items attribute is deprecated. " +
            "Please use the includes attribute.",
            Project.MSG_WARN);
        if (itemString == null || itemString.equals("*") 
            || itemString.equals(".")) {
            createInclude().setName("**");
        } else {
            StringTokenizer tok = new StringTokenizer(itemString, ", ");
            while (tok.hasMoreTokens()) {
                String pattern = tok.nextToken().trim();
                if (pattern.length() > 0) {
                    createInclude().setName(pattern+"/**");
                }
            }
        }
    }
    
    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes(String excludes) {
        fileset.setExcludes(excludes);
    }

    /**
     * List of filenames and directory names to not include. They should be 
     * either , or " " (space) separated. The ignored files will be logged.
     *
     * @param ignoreString the string containing the files to ignore.
     */
    public void XsetIgnore(String ignoreString) {
        log("The ignore attribute is deprecated." + 
            "Please use the excludes attribute.",
            Project.MSG_WARN);
        if (ignoreString != null && ignoreString.length() > 0) {
            Vector tmpExcludes = new Vector();
            StringTokenizer tok = new StringTokenizer(ignoreString, ", ", false);
            while (tok.hasMoreTokens()) {
                createExclude().setName("**/"+tok.nextToken().trim()+"/**");
            }
        }
    }
    
    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions 
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
        this.useDefaultExcludes = useDefaultExcludes;
    }
    
    /**
     * Returns the directory scanner needed to access the files to process.
     */
    protected DirectoryScanner getDirectoryScanner(File baseDir) {
        fileset.setDir(baseDir);
        fileset.setDefaultexcludes(useDefaultExcludes);
        return fileset.getDirectoryScanner(project);
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param includesfile A string containing the filename to fetch
     * the include patterns from.  
     */
     public void setIncludesfile(File includesfile) {
         fileset.setIncludesfile(includesfile);
     }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param excludesfile A string containing the filename to fetch
     * the include patterns from.  
     */
     public void setExcludesfile(File excludesfile) {
         fileset.setExcludesfile(excludesfile);
     }

}
