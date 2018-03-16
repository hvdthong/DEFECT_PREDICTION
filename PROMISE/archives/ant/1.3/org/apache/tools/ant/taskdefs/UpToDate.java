package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Date;
import java.util.Vector;

/**
 * Will set the given property if the specified target has a timestamp
 * greater than all of the source files.
 *
 * @author William Ferguson <a href="mailto:williamf@mincom.com">williamf@mincom.com</a> 
 * @author Hiroaki Nakamura <a href="mailto:hnakamur@mc.neweb.ne.jp">hnakamur@mc.neweb.ne.jp</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class UpToDate extends MatchingTask {

    private String _property;
    private File _targetFile;
    private Vector sourceFileSets = new Vector();

    protected Mapper mapperElement = null;

    /**
     * The property to set if the target file is more up to date than each of
     * the source files.
     *
     * @param property the name of the property to set if Target is up to date.
     */
    public void setProperty(String property) {
        _property = property;
    }

    /**
     * The file which must be more up to date than each of the source files
     * if the property is to be set.
     *
     * @param file the file which we are checking against.
     */
    public void setTargetFile(File file) {
        _targetFile = file;
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
                                     location);
        }
        mapperElement = new Mapper(project);
        return mapperElement;
    }

    /**
     * Sets property to true if target files have a more recent timestamp than
     * each of the corresponding source files.
     */
    public void execute() throws BuildException {

        if (sourceFileSets.size() == 0) {
          throw new BuildException("At least one <srcfiles> element must be set");
        }

        if (_targetFile == null && mapperElement == null) {
          throw new BuildException("The targetfile attribute or a nested mapper element must be set");
        }

        if (_targetFile != null && !_targetFile.exists()) return; 

        Enumeration enum = sourceFileSets.elements();
        boolean upToDate = true;
        while (upToDate && enum.hasMoreElements()) {
            FileSet fs = (FileSet) enum.nextElement();
            DirectoryScanner ds = fs.getDirectoryScanner(project);
            upToDate = upToDate && scanDir(fs.getDir(project), 
                                           ds.getIncludedFiles());
        }

        if (upToDate) {
            this.project.setProperty(_property, "true");
            if (mapperElement == null) {
                log("File \"" + _targetFile.getAbsolutePath() + "\" is up to date.",
                    Project.MSG_VERBOSE);
            } else {
                log("All target files have been up to date.",
                    Project.MSG_VERBOSE);
            }
        }
    }

    protected boolean scanDir(File srcDir, String files[]) {
        SourceFileScanner sfs = new SourceFileScanner(this);
        FileNameMapper mapper = null;
        if (mapperElement == null) {
            MergingMapper mm = new MergingMapper();
            mm.setTo(_targetFile.getAbsolutePath());
            mapper = mm;
        } else {
            mapper = mapperElement.getImplementation();
        }
        return sfs.restrict(files, srcDir, null, mapper).length == 0;
    }
}
