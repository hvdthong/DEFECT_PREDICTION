package org.apache.tools.ant.taskdefs.optional.metamata;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.util.regexp.*;

import java.io.*;
import java.util.*;

/**
 * Metamata Audit evaluates Java code for programming errors, weaknesses, and
 * style violation.
 * <p>
 * Metamata Audit exists in three versions:
 * <ul>
 *  <li>The Lite version evaluates about 15 built-in rules.</li>
 *  <li>The Pro version evaluates about 50 built-in rules.</li>
 *  <li>The Enterprise version allows you to add your own customized rules via the API.</li>
 * <ul>
 * For more information, visit the website at
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MAudit extends AbstractMetamataTask {

    /* As of Metamata 2.0, the command line of MAudit is as follows:
    Usage
        maudit <option>... <path>... [-unused <search-path>...]

    Parameters
        path               File or directory to audit.
        search-path        File or directory to search for declaration uses.

    Options
        -arguments  -A     <file>     Includes command line arguments from file.
        -classpath  -cp    <path>     Sets class path (also source path unless one
                                      explicitly set). Overrides METAPATH/CLASSPATH.
        -exit       -x                Exits after the first error.
        -fix        -f                Automatically fixes certain errors.
        -fullpath                     Prints full path for locations.
        -help       -h                Prints help and exits.
        -list       -l                Creates listing file for each audited file.
        -offsets    -off              Offset and length for locations.
        -output     -o     <file>     Prints output to file.
        -quiet      -q                Suppresses copyright and summary messages.
        -sourcepath        <path>     Sets source path. Overrides SOURCEPATH.
        -tab        -t                Prints a tab character after first argument.
        -unused     -u                Finds declarations unused in search paths.
        -verbose    -v                Prints all messages.
        -version    -V                Prints version and exits.
    */


    /** pattern used by maudit to report the error for a file */
    /** RE does not seems to support regexp pattern with comments so i'm stripping it*/
    static final String AUDIT_PATTERN = "(?:file:)?(.+):(\\d+)\\s*:\\s+(.*)";

    protected File outFile = null;
    
    protected Path searchPath = null;
    
    protected boolean fix = false;
    
    protected boolean list = false;
    
    protected boolean unused = false;

    /** default constructor */
    public MAudit() {
        super("com.metamata.gui.rc.MAudit");
    }

    /** set the destination file which should be an xml file */
    public void setTofile(File outFile){
        this.outFile = outFile;
    }

    public void setFix(boolean flag){
        this.fix = flag;
    }

    public void setList(boolean flag){
        this.list = flag;
    }

    public void setUnused(boolean flag){
        this.unused = flag;
    }

    public Path createSearchpath(){
        if (searchPath == null){
            searchPath = new Path(project);
        }
        return searchPath;
    }

    protected Vector getOptions(){
        Vector options = new Vector(512);
        if (sourcePath != null){
            classPath = sourcePath;
        }        
        
        if (classPath != null){
            options.addElement("-classpath");
            options.addElement(classPath.toString());
        }
        if (fix){
            options.addElement("-fix");
        }
        options.addElement("-fullpath");

        if (list){
            options.addElement("-list");
        }
        if (sourcePath != null){
            options.addElement("-sourcepath");
            options.addElement(sourcePath.toString());
        }
        
        if (unused){
            options.addElement("-unused");
            options.addElement(searchPath.toString());
        }
        addAllVector(options, includedFiles.keys());
        return options;
    }

    protected void checkOptions() throws BuildException {
        super.checkOptions();
        if (unused && searchPath == null){
            throw new BuildException("'searchpath' element must be set when looking for 'unused' declarations.");
        }
        if (!unused && searchPath != null){
            log("'searchpath' element ignored. 'unused' attribute is disabled.", Project.MSG_WARN);
        }
    }
    
    protected ExecuteStreamHandler createStreamHandler() throws BuildException {
        ExecuteStreamHandler handler = null;
        if (outFile == null){
           handler = new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_INFO);
        } else {
            try {
                OutputStream out = new FileOutputStream( outFile );
                handler = new MAuditStreamHandler(this, out);
            } catch (IOException e){
                throw new BuildException(e);
            }
        }
        return handler;
    }

    protected void cleanUp() throws BuildException {
        super.cleanUp();

        /*if (out != null){
        }*/
    }

    /** the inner class used to report violation information */
    static final class Violation {
        int line;
        String error;
    }

    /** handy factory to create a violation */
    static final Violation createViolation(int line, String msg){
        Violation violation = new Violation();
        violation.line = line;
        violation.error = msg;
        return violation;
    }

}


