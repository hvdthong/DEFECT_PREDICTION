package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.SourceFileScanner;

/**
 * Sets the given property if the specified target has a timestamp
 * greater than all of the source files.
 *
 * @author William Ferguson 
 *         <a href="mailto:williamf@mincom.com">williamf@mincom.com</a> 
 * @author Hiroaki Nakamura 
 *         <a href="mailto:hnakamur@mc.neweb.ne.jp">hnakamur@mc.neweb.ne.jp</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @since Ant 1.2
 *
 * @ant.task category="control"
 */

public class UpToDate extends Task implements Condition {

    private String _property;
    private String _value;
    private File _sourceFile;
    private File _targetFile;
    private Vector sourceFileSets = new Vector();

    protected Mapper mapperElement = null;

    /**
     * The property to set if the target file is more up-to-date than
     * (each of) the source file(s).
     *
     * @param property the name of the property to set if Target is up-to-date.
     */
    public void setProperty(String property) {
        _property = property;
    }

    /**
     * The value to set the named property to if the target file is more
     * up-to-date than (each of) the source file(s). Defaults to 'true'.
     *
     * @param value the value to set the property to if Target is up-to-date
     */
    public void setValue(String value) {
        _value = value;
    }

    /**
     * Returns the value, or "true" if a specific value wasn't provided.
     */
    private String getValue() {
        return (_value != null) ? _value : "true";
    } 

    /**
     * The file which must be more up-to-date than (each of) the source file(s)
     * if the property is to be set.
     *
     * @param file the file we are checking against.
     */
    public void setTargetFile(File file) {
        _targetFile = file;
    }

    /**
     * The file that must be older than the target file
     * if the property is to be set.
     *
     * @param file the file we are checking against the target file.
     */
    public void setSrcfile(File file) {
        _sourceFile = file;
    }

    /**
     * Nested &lt;srcfiles&gt; element.
     */
    public void addSrcfiles(FileSet fs) {
        sourceFileSets.addElement(fs);
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     */
    public Mapper createMapper() throws BuildException {
        if (mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper",
                                     getLocation());
        }
        mapperElement = new Mapper(getProject());
        return mapperElement;
    }

    /**
     * Evaluate (all) target and source file(s) to
     * see if the target(s) is/are up-to-date.
     */
    public boolean eval() {
        if (sourceFileSets.size() == 0 && _sourceFile == null) {
            throw new BuildException("At least one srcfile or a nested "
                                     + "<srcfiles> element must be set.");
        }

        if (sourceFileSets.size() > 0 && _sourceFile != null) {
            throw new BuildException("Cannot specify both the srcfile "
                                     + "attribute and a nested <srcfiles> "
                                     + "element.");
        }

        if (_targetFile == null && mapperElement == null) {
            throw new BuildException("The targetfile attribute or a nested "
                                     + "mapper element must be set.");
        }

        if (_targetFile != null && !_targetFile.exists()) {
            return false;
        } 

        if (_sourceFile != null && !_sourceFile.exists()) {
            throw new BuildException(_sourceFile.getAbsolutePath() 
                                     + " not found.");
        }

        Enumeration enum = sourceFileSets.elements();
        boolean upToDate = true;
        while (upToDate && enum.hasMoreElements()) {
            FileSet fs = (FileSet) enum.nextElement();
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            upToDate = upToDate && scanDir(fs.getDir(getProject()),
                                           ds.getIncludedFiles());
        }

        if (_sourceFile != null) {
            if (mapperElement == null) {
                upToDate = upToDate &&
                    (_targetFile.lastModified() >= _sourceFile.lastModified());
            } else {
                SourceFileScanner sfs = new SourceFileScanner(this);
                upToDate = upToDate &&
                    (sfs.restrict(new String[] {_sourceFile.getAbsolutePath()},
                                  null, null,
                                  mapperElement.getImplementation())
                     .length == 0);
            }
        }
        return upToDate;
    }


    /**
     * Sets property to true if target file(s) have a more recent timestamp
     * than (each of) the corresponding source file(s).
     */
    public void execute() throws BuildException {
        if (_property == null) {
            throw new BuildException("property attribute is required.", 
                                     getLocation());
        }
        boolean upToDate = eval();
        if (upToDate) {
            this.getProject().setNewProperty(_property, getValue());
            if (mapperElement == null) {
                log("File \"" + _targetFile.getAbsolutePath() 
                    + "\" is up-to-date.", Project.MSG_VERBOSE);
            } else {
                log("All target files are up-to-date.",
                    Project.MSG_VERBOSE);
            }
        }
    }

    protected boolean scanDir(File srcDir, String[] files) {
        SourceFileScanner sfs = new SourceFileScanner(this);
        FileNameMapper mapper = null;
        File dir = srcDir;
        if (mapperElement == null) {
            MergingMapper mm = new MergingMapper();
            mm.setTo(_targetFile.getAbsolutePath());
            mapper = mm;
            dir = null;
        } else {
            mapper = mapperElement.getImplementation();
        }
        return sfs.restrict(files, srcDir, dir, mapper).length == 0;
    }
}
