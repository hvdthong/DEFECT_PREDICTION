package org.apache.tools.ant.types;

import java.util.Iterator;

import org.apache.tools.ant.types.resources.FileResourceIterator;

/**
 * Moved out of MatchingTask to make it a standalone object that could
 * be referenced (by scripts for example).
 *
 */
public class FileSet extends AbstractFileSet implements ResourceCollection {

    /**
     * Constructor for FileSet.
     */
    public FileSet() {
        super();
    }

    /**
     * Constructor for FileSet, with FileSet to shallowly clone.
     * @param fileset the fileset to clone
     */
    protected FileSet(FileSet fileset) {
        super(fileset);
    }

    /**
     * Return a FileSet that has the same basedir and same patternsets
     * as this one.
     * @return the cloned fileset
     */
    public Object clone() {
        if (isReference()) {
            return ((FileSet) getRef(getProject())).clone();
        } else {
            return super.clone();
        }
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
        return new FileResourceIterator(getDir(getProject()),
            getDirectoryScanner(getProject()).getIncludedFiles());
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
        return getDirectoryScanner(getProject()).getIncludedFilesCount();
    }

    /**
     * Always returns true.
     * @return true indicating that all elements will be FileResources.
     * @since Ant 1.7
     */
    public boolean isFilesystemOnly() {
        return true;
    }

}
