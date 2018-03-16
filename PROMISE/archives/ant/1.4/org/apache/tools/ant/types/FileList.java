package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import java.io.File;

import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

/**
 * FileList represents an explicitly named list of files.  FileLists
 * are useful when you want to capture a list of files regardless of
 * whether they currently exist.  By contrast, FileSet operates as a
 * filter, only returning the name of a matched file if it currently
 * exists in the file system.
 * 
 * @author <a href="mailto:cstrong@arielpartners.com">Craeg Strong</a>
 * @version $Revision: 269479 $ $Date: 2001-08-06 22:32:46 +0800 (周一, 2001-08-06) $
 */
public class FileList extends DataType {
    
    private Vector filenames = new Vector();
    private File dir;

    public FileList() {
        super();
    }

    protected FileList(FileList filelist) {
        this.dir       = filelist.dir;
        this.filenames = filelist.filenames;
    }

    /**
     * Makes this instance in effect a reference to another FileList
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p> 
     */
    public void setRefid(Reference r) throws BuildException {
        if ((dir != null) || (filenames.size() != 0)) {
            throw tooManyAttributes();
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

    public void setFiles(String filenames) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (filenames != null && filenames.length() > 0) {
            StringTokenizer tok = new StringTokenizer(filenames, ", \t\n\r\f", false);
            while (tok.hasMoreTokens()) {
               this.filenames.addElement(tok.nextToken());
            }
        }
    }

    /**
     * Returns the list of files represented by this FileList.
     */
    public String[] getFiles(Project p) {
        if (isReference()) {
            return getRef(p).getFiles(p);
        }

        if (dir == null) {
            throw new BuildException("No directory specified for filelist.");
        }

        if (filenames.size() == 0) {
            throw new BuildException("No files specified for filelist.");
        }

        String result[] = new String[filenames.size()];
        filenames.copyInto(result);
        return result;
    }
    
    /**
     * Performs the check for circular references and returns the
     * referenced FileList.  
     */
    protected FileList getRef(Project p) {
        if (!checked) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, p);
        }
        
        Object o = ref.getReferencedObject(p);
        if (!(o instanceof FileList)) {
            String msg = ref.getRefId()+" doesn\'t denote a filelist";
            throw new BuildException(msg);
        } else {
            return (FileList) o;
        }
    }

