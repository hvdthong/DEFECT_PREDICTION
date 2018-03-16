package org.apache.tools.ant.types.selectors;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * Selector that filters files based on whether they are newer than
 * a matching file in another directory tree. It can contain a mapper
 * element, so isn't available as an ExtendSelector (since those
 * parameters can't hold other elements).
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public class DependSelector extends BaseSelector {

    private File targetdir = null;
    private Mapper mapperElement = null;
    private FileNameMapper map = null;
    private int granularity = 0;

    public DependSelector() {
        if (Os.isFamily("dos")) {
            granularity = 2000;
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("{dependselector targetdir: ");
        if (targetdir == null) {
            buf.append("NOT YET SET");
        }
        else {
            buf.append(targetdir.getName());
        }
        buf.append(" granularity: ");
        buf.append(granularity);
        if (map != null) {
            buf.append(" mapper: ");
            buf.append(map.toString());
        }
        else if (mapperElement != null) {
            buf.append(" mapper: ");
            buf.append(mapperElement.toString());
        }
        buf.append("}");
        return buf.toString();
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
     * Sets the number of milliseconds leeway we will give before we consider
     * a file out of date.
     */
    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    /**
     * Defines the FileNameMapper to use (nested mapper element).
     */
    public Mapper createMapper() throws BuildException {
        if (mapperElement != null) {
            throw new BuildException("Cannot define more than one mapper");
        }
        mapperElement = new Mapper(project);
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
        }
        else {
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
        File destfile = new File(targetdir,destname);

        return SelectorUtils.isOutOfDate(file, destfile, granularity);
    }

}

