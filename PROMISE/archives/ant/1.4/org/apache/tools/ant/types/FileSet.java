package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;

import java.io.File;
import java.util.Stack;
import java.util.Vector;

/**
 * Moved out of MatchingTask to make it a standalone object that could
 * be referenced (by scripts for example).
 *
 * @author Arnout J. Kuiper <a href="mailto:ajkuiper@wxs.nl">ajkuiper@wxs.nl</a> 
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:umagesh@rediffmail.com">Magesh Umasankar</a>
 */
public class FileSet extends DataType {
    
    private PatternSet defaultPatterns = new PatternSet();
    private Vector additionalPatterns = new Vector();

    private File dir;
    private boolean useDefaultExcludes = true;
    private boolean isCaseSensitive = true;

    public FileSet() {
        super();
    }

    protected FileSet(FileSet fileset) {
        this.dir = fileset.dir;
        this.defaultPatterns = fileset.defaultPatterns;
        this.additionalPatterns = fileset.additionalPatterns;
        this.useDefaultExcludes = fileset.useDefaultExcludes;
        this.isCaseSensitive = fileset.isCaseSensitive;
    }
    
    

    /**
     * Makes this instance in effect a reference to another PatternSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p> 
     */
    public void setRefid(Reference r) throws BuildException {
        if (dir != null || defaultPatterns.hasPatterns()) {
            throw tooManyAttributes();
        }
        if (!additionalPatterns.isEmpty()) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    public void setDir(File dir) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }

        this.dir = dir;
    }

    public File getDir(Project p) {
        if (isReference()) {
            return getRef(p).getDir(p);
        }
        return dir;
    }

    public PatternSet createPatternSet() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        PatternSet patterns = new PatternSet();
        additionalPatterns.addElement(patterns);
        return patterns;
    }

    /**
     * add a name entry on the include list
     */
    public PatternSet.NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createInclude();
    }
    
    /**
     * add a name entry on the include files list
     */
    public PatternSet.NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createIncludesFile();
    }
    
    /**
     * add a name entry on the exclude list
     */
    public PatternSet.NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createExclude();
    }

    /**
     * add a name entry on the include files list
     */
    public PatternSet.NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        return defaultPatterns.createExcludesFile();
    }
    
    /**
     * Sets the set of include patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param includes the string containing the include patterns
     */
    public void setIncludes(String includes) {
        if (isReference()) {
            throw tooManyAttributes();
        }

        defaultPatterns.setIncludes(includes);
    }

    /**
     * Sets the set of exclude patterns. Patterns may be separated by a comma
     * or a space.
     *
     * @param excludes the string containing the exclude patterns
     */
    public void setExcludes(String excludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }

        defaultPatterns.setExcludes(excludes);
    }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param incl The file to fetch the include patterns from.  
     */
     public void setIncludesfile(File incl) throws BuildException {
         if (isReference()) {
             throw tooManyAttributes();
         }

         defaultPatterns.setIncludesfile(incl);
     }

    /**
     * Sets the name of the file containing the includes patterns.
     *
     * @param excl The file to fetch the exclude patterns from.  
     */
     public void setExcludesfile(File excl) throws BuildException {
         if (isReference()) {
             throw tooManyAttributes();
         }

         defaultPatterns.setExcludesfile(excl);
     }

    /**
     * Sets whether default exclusions should be used or not.
     *
     * @param useDefaultExcludes "true"|"on"|"yes" when default exclusions 
     *                           should be used, "false"|"off"|"no" when they
     *                           shouldn't be used.
     */
    public void setDefaultexcludes(boolean useDefaultExcludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }

        this.useDefaultExcludes = useDefaultExcludes;
    }

    /**
     * Sets case sensitivity of the file system
     *
     * @param isCaseSensitive "true"|"on"|"yes" if file system is case
     *                           sensitive, "false"|"off"|"no" when not.
     */
    public void setCaseSensitive(boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }

    /**
     * Returns the directory scanner needed to access the files to process.
     */
    public DirectoryScanner getDirectoryScanner(Project p) {
        if (isReference()) {
            return getRef(p).getDirectoryScanner(p);
        }

        if (dir == null) {
            throw new BuildException("No directory specified for fileset.");
        }

        if (!dir.exists()) {
            throw new BuildException(dir.getAbsolutePath()+" not found.");
        }
        if (!dir.isDirectory()) {
            throw new BuildException(dir.getAbsolutePath()+" is not a directory.");
        }

        DirectoryScanner ds = new DirectoryScanner();
        setupDirectoryScanner(ds, p);
        ds.scan();
        return ds;
    }
    
    public void setupDirectoryScanner(FileScanner ds, Project p) {
        if (ds == null) {
            throw new IllegalArgumentException("ds cannot be null");
        }
        
        ds.setBasedir(dir);

        for (int i=0; i<additionalPatterns.size(); i++) {
            Object o = additionalPatterns.elementAt(i);
            defaultPatterns.append((PatternSet) o, p);
        }

        p.log( "FileSet: Setup file scanner in dir " + dir + 
            " with " + defaultPatterns, p.MSG_DEBUG );
        
        ds.setIncludes(defaultPatterns.getIncludePatterns(p));
        ds.setExcludes(defaultPatterns.getExcludePatterns(p));
        if (useDefaultExcludes) ds.addDefaultExcludes();
        ds.setCaseSensitive(isCaseSensitive);
    }

    /**
     * Performs the check for circular references and returns the
     * referenced FileSet.  
     */
    protected FileSet getRef(Project p) {
        if (!checked) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, p);
        }
        
        Object o = ref.getReferencedObject(p);
        if (!(o instanceof FileSet)) {
            String msg = ref.getRefId()+" doesn\'t denote a fileset";
            throw new BuildException(msg);
        } else {
            return (FileSet) o;
        }
    }

}
