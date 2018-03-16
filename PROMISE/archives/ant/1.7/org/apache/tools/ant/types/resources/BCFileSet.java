package org.apache.tools.ant.types.resources;

import java.util.Iterator;

import org.apache.tools.ant.types.FileSet;

/**
 * Utility FileSet that includes directories for backwards-compatibility
 * with certain tasks e.g. Delete.
 * @since Ant 1.7
 */
public class BCFileSet extends FileSet {
    /**
     * Default constructor.
     */
    public BCFileSet() {
    }

    /**
     * Construct a new BCFileSet from the specified FileSet.
     * @param fs the FileSet from which to inherit config.
     */
    public BCFileSet(FileSet fs) {
        super(fs);
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    public Iterator iterator() {
        if (isReference()) {
            return ((FileSet) getRef(getProject())).iterator();
        }
        FileResourceIterator result = new FileResourceIterator(getDir());
        result.addFiles(getDirectoryScanner().getIncludedFiles());
        result.addFiles(getDirectoryScanner().getIncludedDirectories());
        return result;
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return number of elements as int.
     * @since Ant 1.7
     */
    public int size() {
        if (isReference()) {
            return ((FileSet) getRef(getProject())).size();
        }
        return getDirectoryScanner().getIncludedFilesCount()
            + getDirectoryScanner().getIncludedDirsCount();
    }

}
