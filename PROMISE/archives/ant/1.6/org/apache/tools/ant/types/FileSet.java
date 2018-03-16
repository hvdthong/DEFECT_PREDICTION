package org.apache.tools.ant.types;

/**
 * Moved out of MatchingTask to make it a standalone object that could
 * be referenced (by scripts for example).
 *
 */
public class FileSet extends AbstractFileSet {

    public FileSet() {
        super();
    }

    protected FileSet(FileSet fileset) {
        super(fileset);
    }

    /**
     * Return a FileSet that has the same basedir and same patternsets
     * as this one.
     */
    public Object clone() {
        if (isReference()) {
            return ((FileSet) getRef(getProject())).clone();
        } else {
            return super.clone();
        }
    }

}
