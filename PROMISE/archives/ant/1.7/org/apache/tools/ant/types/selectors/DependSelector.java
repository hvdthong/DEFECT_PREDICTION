package org.apache.tools.ant.types.selectors;

import java.io.File;

/**
 * Selector that filters files based on whether they are newer than
 * a matching file in another directory tree. It can contain a mapper
 * element, so isn't available as an ExtendSelector (since those
 * parameters can't hold other elements).
 *
 * @since 1.5
 */
public class DependSelector extends MappingSelector {

    /**
     * Creates a new <code>DependSelector</code> instance.
     *
     */
    public DependSelector() {

    }

    /**
     * @return a string describing this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("{dependselector targetdir: ");
        if (targetdir == null) {
            buf.append("NOT YET SET");
        } else {
            buf.append(targetdir.getName());
        }
        buf.append(" granularity: ");
        buf.append(granularity);
        if (map != null) {
            buf.append(" mapper: ");
            buf.append(map.toString());
        } else if (mapperElement != null) {
            buf.append(" mapper: ");
            buf.append(mapperElement.toString());
        }
        buf.append("}");
        return buf.toString();
    }


    /**
     * this test is our selection test that compared the file with the destfile
     * @param srcfile the source file
     * @param destfile the destination file
     * @return true if destination is out of date
     */
    public boolean selectionTest(File srcfile, File destfile) {
        boolean selected = SelectorUtils.isOutOfDate(srcfile, destfile,
                granularity);
        return selected;
    }

}

