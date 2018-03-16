package org.apache.tools.ant.types;

/**
 * Subclass as hint for supporting tasks that the included directories
 * instead of files should be used.
 *
 * @since Ant 1.5
 */
public class DirSet extends AbstractFileSet {
    
    public DirSet() {
        super();
    }

    protected DirSet(DirSet dirset) {
        super(dirset);
    }

    /**
     * Return a DirSet that has the same basedir and same patternsets
     * as this one.
     */
    public Object clone() {
        if (isReference()) {
            return new DirSet((DirSet) getRef(getProject()));
        } else {
            return new DirSet(this);
        }
    }

}
