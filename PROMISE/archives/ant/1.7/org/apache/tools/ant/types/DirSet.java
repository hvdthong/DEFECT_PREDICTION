package org.apache.tools.ant.types;

import java.util.Iterator;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.resources.FileResourceIterator;

/**
 * Subclass as hint for supporting tasks that the included directories
 * instead of files should be used.
 *
 * @since Ant 1.5
 */
public class DirSet extends AbstractFileSet implements ResourceCollection {

    /**
     * Constructor for DirSet.
     */
    public DirSet() {
        super();
    }

    /**
     * Constructor for DirSet, with DirSet to shallowly clone.
     * @param dirset the dirset to clone.
     */
    protected DirSet(DirSet dirset) {
        super(dirset);
    }

    /**
     * Return a DirSet that has the same basedir and same patternsets
     * as this one.
     * @return the cloned dirset.
     */
    public Object clone() {
        if (isReference()) {
            return ((DirSet) getRef(getProject())).clone();
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
            return ((DirSet) getRef(getProject())).iterator();
        }
        return new FileResourceIterator(getDir(getProject()),
            getDirectoryScanner(getProject()).getIncludedDirectories());
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return number of elements as int.
     * @since Ant 1.7
     */
    public int size() {
        if (isReference()) {
            return ((DirSet) getRef(getProject())).size();
        }
        return getDirectoryScanner(getProject()).getIncludedDirsCount();
    }

    /**
     * Always returns true.
     * @return true indicating that all elements will be FileResources.
     * @since Ant 1.7
     */
    public boolean isFilesystemOnly() {
        return true;
    }

    /**
     * Returns included directories as a list of semicolon-separated paths.
     *
     * @return a <code>String</code> of included directories.
     */
    public String toString() {
        DirectoryScanner ds = getDirectoryScanner(getProject());
        String[] dirs = ds.getIncludedDirectories();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < dirs.length; i++) {
            if (i > 0) {
                sb.append(';');
            }
            sb.append(dirs[i]);
        }
        return sb.toString();
    }

}
