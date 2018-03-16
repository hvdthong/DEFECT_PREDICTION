package org.apache.tools.ant.taskdefs.optional.sitraka;

import java.util.Vector;

/**
 * Filters information from coverage, somewhat similar to a <tt>FileSet</tt>.
 *
 */
public class Filters {

    /** default regexp to exclude everything */
    public static final String DEFAULT_EXCLUDE = "*.*():E";

    /** say whether we should use the default excludes or not */
    protected boolean defaultExclude = true;

    /** user defined filters */
    protected Vector filters = new Vector();

    public Filters() {
    }

    /**
     * Automatically exclude all classes and methods
     * unless included in nested elements; optional, default true.
     */
    public void setDefaultExclude(boolean value) {
        defaultExclude = value;
    }

    /**
     * include classes and methods in the analysis
     */
    public void addInclude(Include incl) {
        filters.addElement(incl);
    }

    /**
     * exclude classes and methods from the analysis
     */
    public void addExclude(Exclude excl) {
        filters.addElement(excl);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        final int size = filters.size();
        if (defaultExclude) {
            buf.append(DEFAULT_EXCLUDE);
            if (size > 0) {
                buf.append(',');
            }
        }
        for (int i = 0; i < size; i++) {
            buf.append(filters.elementAt(i).toString());
            if (i < size - 1) {
                buf.append(',');
            }
        }
        return buf.toString();
    }

    /**
     * an includes or excludes element
     */
    public abstract static class FilterElement {
        protected String clazz;

        /**
         * this one is deprecated.
         * @ant.task ignore="true"
         */

        public void setName(String value) {
            clazz = value;
        }

        /**
         * The classname mask as a simple regular expression;
         * optional, defaults to "*"
         */
        public void setClass(String value) {
            clazz = value;
        }

        /**
         * The method mask as a simple regular expression;
         * optional, defaults to "*"
         */
        public void setMethod(String value) {
            method = value;
        }

        /**
         * enable or disable the filter; optional, default true
         */

        public void setEnabled(boolean value) {
            enabled = value;
        }

        public String toString() {
            return clazz + "." + method + "()";
        }
    }

    public static class Include extends FilterElement {
        public String toString() {
            return super.toString() + ":I" + (enabled ? "" : "#");
        }
    }

    public static class Exclude extends FilterElement {
        public String toString() {
            return super.toString() + ":E" + (enabled ? "" : "#");
        }
    }
}



