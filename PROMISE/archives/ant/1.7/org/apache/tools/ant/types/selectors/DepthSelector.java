package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;

/**
 * Selector that filters files based on the how deep in the directory
 * tree they are.
 *
 * @since 1.5
 */
public class DepthSelector extends BaseExtendSelector {


    /** min attribute */
    public int min = -1;
    /** max attribute */
    public int max = -1;


    /** Used for parameterized custom selector */
    public static final String MIN_KEY = "min";
    /** Used for parameterized custom selector */
    public static final String MAX_KEY = "max";

    /**
     * Creates a new <code>DepthSelector</code> instance.
     *
     */
    public DepthSelector() {
    }

    /**
     * @return a string describing this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("{depthselector min: ");
        buf.append(min);
        buf.append(" max: ");
        buf.append(max);
        buf.append("}");
        return buf.toString();
    }

    /**
     * The minimum depth below the basedir before a file is selected.
     *
     * @param min minimum directory levels below basedir to go
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * The minimum depth below the basedir before a file is selected.
     *
     * @param max maximum directory levels below basedir to go
     */
    public void setMax(int max) {
        this.max = max;
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
                if (MIN_KEY.equalsIgnoreCase(paramname)) {
                    try {
                        setMin(Integer.parseInt(parameters[i].getValue()));
                    } catch (NumberFormatException nfe1) {
                        setError("Invalid minimum value "
                                + parameters[i].getValue());
                    }
                } else if (MAX_KEY.equalsIgnoreCase(paramname)) {
                    try {
                        setMax(Integer.parseInt(parameters[i].getValue()));
                    } catch (NumberFormatException nfe1) {
                        setError("Invalid maximum value "
                                + parameters[i].getValue());
                    }
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * Checks to make sure all settings are kosher. In this case, it
     * means that the max depth is not lower than the min depth.
     */
    public void verifySettings() {
        if (min < 0 && max < 0) {
            setError("You must set at least one of the min or the "
                    + "max levels.");
        }
        if (max < min && max > -1) {
            setError("The maximum depth is lower than the minimum.");
        }
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a file in a particular fileset. Most of the work
     * for this selector is offloaded into SelectorUtils, a static class
     * that provides the same services for both FilenameSelector and
     * DirectoryScanner.
     *
     * @param basedir the base directory the scan is being done from
     * @param filename is the name of the file to check
     * @param file is a java.io.File object the selector can use
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {

        validate();

        int depth = -1;
        String absBase = basedir.getAbsolutePath();
        String absFile = file.getAbsolutePath();
        StringTokenizer tokBase = new StringTokenizer(absBase,
                File.separator);
        StringTokenizer tokFile = new StringTokenizer(absFile,
                File.separator);
        while (tokFile.hasMoreTokens()) {
            String filetoken = tokFile.nextToken();
            if (tokBase.hasMoreTokens()) {
                String basetoken = tokBase.nextToken();
                if (!basetoken.equals(filetoken)) {
                    throw new BuildException("File " + filename
                            + " does not appear within " + absBase
                            + "directory");
                }
            } else {
                depth += 1;
                if (max > -1 && depth > max) {
                    return false;
                }
            }
        }
        if (tokBase.hasMoreTokens()) {
            throw new BuildException("File " + filename
                + " is outside of " + absBase + "directory tree");
        }
        if (min > -1 && depth < min) {
            return false;
        }
        return true;
    }

}

