package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;

/**
 * A mapping selector is an abstract class adding mapping support to the base
 * selector
 */
public abstract class MappingSelector extends BaseSelector {

    /** Utilities used for file operations */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();


    protected File targetdir = null;
    protected Mapper mapperElement = null;
    protected FileNameMapper map = null;
    protected int granularity = 0;


    /**
     * Creates a new <code>MappingSelector</code> instance.
     *
     */
    public MappingSelector() {
        granularity = (int) FILE_UTILS.getFileTimestampGranularity();
    }


    /**
     * The name of the file or directory which is checked for out-of-date
     * files.
     *
     * @param targetdir the directory to scan looking for files.
     */
    public void setTargetdir(File targetdir) {
        this.targetdir = targetdir;
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     * @return a mapper to be configured
     * @throws BuildException if more that one mapper defined
     */
    public Mapper createMapper() throws BuildException {
        if (mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper");
        }
        mapperElement = new Mapper(getProject());
        return mapperElement;
    }

    /**
     * Checks to make sure all settings are kosher. In this case, it
     * means that the dest attribute has been set and we have a mapper.
     */
    public void verifySettings() {
        if (targetdir == null) {
            setError("The targetdir attribute is required.");
        }
        if (mapperElement == null) {
            map = new IdentityMapper();
        } else {
            map = mapperElement.getImplementation();
        }
        if (map == null) {
            setError("Could not set <mapper> element.");
        }
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a file in a particular fileset.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file is a java.io.File object the selector can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {

        validate();

        String[] destfiles = map.mapFileName(filename);
        if (destfiles == null) {
            return false;
        }
        if (destfiles.length != 1 || destfiles[0] == null) {
            throw new BuildException("Invalid destination file results for "
                    + targetdir.getName() + " with filename " + filename);
        }
        String destname = destfiles[0];
        File destfile = new File(targetdir, destname);

        boolean selected = selectionTest(file, destfile);
        return selected;
    }

    /**
     * this test is our selection test that compared the file with the destfile
     * @param srcfile file to test; may be null
     * @param destfile destination file
     * @return true if source file compares with destination file
     */
    protected abstract boolean selectionTest(File srcfile, File destfile);

    /**
     * Sets the number of milliseconds leeway we will give before we consider
     * a file out of date. Defaults to 2000 on MS-DOS derivatives and 1000 on
     * others.
     * @param granularity the leeway in milliseconds
     */
    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

}
