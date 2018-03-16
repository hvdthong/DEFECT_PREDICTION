package org.apache.tools.ant.types.selectors;

import java.io.File;

import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;

/**
 * Selector that selects a certain kind of file: directory or regular.
 *
 * @since 1.6
 */
public class TypeSelector extends BaseExtendSelector {

    private String type = null;

    /** Key to used for parameterized custom selector */
    public static final String TYPE_KEY = "type";

    /**
     * Creates a new <code>TypeSelector</code> instance.
     *
     */
    public TypeSelector() {
    }

    /**
     * @return a string describing this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("{typeselector type: ");
        buf.append(type);
        buf.append("}");
        return buf.toString();
    }

    /**
     * Set the type of file to require.
     * @param fileTypes the type of file - file or dir
     */
    public void setType(FileType fileTypes) {
        this.type = fileTypes.getValue();
    }

    /**
     * When using this as a custom selector, this method will be called.
     * It translates each parameter into the appropriate setXXX() call.
     *
     * @param parameters the complete set of parameters for this selector
     */
    public void setParameters(Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                String paramname = parameters[i].getName();
                if (TYPE_KEY.equalsIgnoreCase(paramname)) {
                    FileType type = new FileType();
                    type.setValue(parameters[i].getValue());
                    setType(type);
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * Checks to make sure all settings are kosher. In this case, it
     * means that the pattern attribute has been set.
     *
     */
    public void verifySettings() {
        if (type == null) {
            setError("The type attribute is required");
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

        if (file.isDirectory()) {
            return type.equals(FileType.DIR);
        } else {
            return type.equals(FileType.FILE);
        }
    }

    /**
     * Enumerated attribute with the values for types of file
     */
    public static class FileType extends EnumeratedAttribute {
        /** the string value for file */
        public static final String FILE = "file";
        /** the string value for dir */
        public static final String DIR = "dir";

        /**
         * @return the values as an array of strings
         */
        public String[] getValues() {
            return new String[]{FILE, DIR};
        }
    }


}
