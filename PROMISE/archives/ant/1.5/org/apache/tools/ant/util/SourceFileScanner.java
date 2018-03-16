package org.apache.tools.ant.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.taskdefs.condition.Os;

import java.io.File;
import java.util.Vector;

/**
 * Utility class that collects the functionality of the various
 * scanDir methods that have been scattered in several tasks before.
 *
 * <p>The only method returns an array of source files. The array is a
 * subset of the files given as a parameter and holds only those that
 * are newer than their corresponding target files.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:levylambert@tiscali-dsl.de">Antoine Levy-Lambert</a>
 */
public class SourceFileScanner implements ResourceFactory {

    protected Task task;

    private FileUtils fileUtils;

    /**
     * @param task The task we should log messages through
     */
    public SourceFileScanner(Task task) {
        this.task = task;
        fileUtils = FileUtils.newFileUtils();
    }

    /**
     * Restrict the given set of files to those that are newer than
     * their corresponding target files.
     *
     * @param files   the original set of files
     * @param srcDir  all files are relative to this directory
     * @param destDir target files live here. if null file names
     *                returned by the mapper are assumed to be absolute.
     * @param mapper  knows how to construct a target file names from
     *                source file names.
     */
    public String[] restrict(String[] files, File srcDir, File destDir,
                             FileNameMapper mapper) {
        this.destDir = destDir;
        Vector v = new Vector();
        for (int i = 0; i < files.length; i++) {
            File src = fileUtils.resolveFile(srcDir, files[i]);
            v.addElement(new Resource(files[i], src.exists(),
                                      src.lastModified(), src.isDirectory()));
        }
        Resource[] sourceresources= new Resource[v.size()];
        v.copyInto(sourceresources);

        Resource[] outofdate = 
            ResourceUtils.selectOutOfDateSources(task, sourceresources,
                                                 mapper, this);
        String[] result = new String[outofdate.length];
        for (int counter=0; counter < outofdate.length; counter++) {
            result[counter] = outofdate[counter].getName();
        }
        return result;
    }

    /**
     * Convinience layer on top of restrict that returns the source
     * files as File objects (containing absolute paths if srcDir is
     * absolute).
     */
    public File[] restrictAsFiles(String[] files, File srcDir, File destDir,
                                  FileNameMapper mapper) {
        String[] res = restrict(files, srcDir, destDir, mapper);
        File[] result = new File[res.length];
        for (int i = 0; i < res.length; i++) {
            result[i] = new File(srcDir, res[i]);
        }
        return result;
    }

    /**
     * returns resource information for a file at destination
     * @param name relative path of file at destination
     * @return data concerning a file whose relative path to destDir is name
     *
     * @since Ant 1.5.2
     */
    public Resource getResource(String name) {
        File src = fileUtils.resolveFile(destDir, name);
        return new Resource(name, src.exists(), src.lastModified(),
                            src.isDirectory());
    }
}
