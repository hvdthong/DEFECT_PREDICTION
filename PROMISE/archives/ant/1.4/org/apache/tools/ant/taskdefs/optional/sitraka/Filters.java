package org.apache.tools.ant.taskdefs.optional.sitraka;

import java.util.Vector;

/**
 * Filters information from coverage, somewhat similar to a <tt>FileSet</tt>.
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class Filters {

    /** default regexp to exclude everything */
    public final static String DEFAULT_EXCLUDE = "*.*():E";

    /** say whether we should use the default excludes or not */
    protected boolean defaultExclude = true;

    /** user defined filters */
    protected Vector filters = new Vector();

    public Filters(){
    }

    public void setDefaultExclude(boolean value){
        defaultExclude = value;
    }

    public void addInclude(Include incl){
        filters.addElement(incl);
    }

    public void addExclude(Exclude excl){
        filters.addElement(excl);
    }

    public String toString(){
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
            if ( i < size - 1) {
                buf.append(',');
            }
        }
        return buf.toString();
    }

    public static abstract class FilterElement {
        protected String clazz;
            clazz = value;
        }
        public void setClass(String value){
            clazz  = value;
        }
        public void setMethod(String value){
            method = value;
        }
        public void setEnabled(boolean value){
            enabled = value;
        }
        public String toString(){
            return clazz + "." + method + "()";
        }
    }

    public static class Include extends FilterElement {
        public String toString(){
            return super.toString() + ":I" + (enabled ? "" : "#");
        }
    }

    public static class Exclude extends FilterElement {
        public String toString() {
            return super.toString() + ":E" + (enabled ? "" : "#");
        }
    }
}



