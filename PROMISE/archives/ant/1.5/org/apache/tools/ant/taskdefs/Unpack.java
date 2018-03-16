package org.apache.tools.ant.taskdefs;


import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Abstract Base class for unpack tasks.
 *
 * @author Magesh Umasankar
 *
 * @since Ant 1.5
 */

public abstract class Unpack extends Task {

    protected File source;
    protected File dest;

    /**
     * @deprecated setSrc(String) is deprecated and is replaced with
     *             setSrc(File) to make Ant's Introspection
     *             mechanism do the work and also to encapsulate operations on
     *             the type in its own class.
     * @ant.attribute ignore="true"
     */
    public void setSrc(String src) {
        log("DEPRECATED - The setSrc(String) method has been deprecated."
            + " Use setSrc(File) instead.");
        setSrc(project.resolveFile(src));
    }

    /**
     * @deprecated setDest(String) is deprecated and is replaced with
     *             setDest(File) to make Ant's Introspection
     *             mechanism do the work and also to encapsulate operations on
     *             the type in its own class.
     * @ant.attribute ignore="true"
     */
    public void setDest(String dest) {
        log("DEPRECATED - The setDest(String) method has been deprecated."
            + " Use setDest(File) instead.");
        setDest(project.resolveFile(dest));
    }

    /**
     * The file to expand; required.
     * @param src file to expand
     */
    public void setSrc(File src) {
        source = src;
    }

    /**
     * The destination file or directory; optional.
     * @param dest destination file or directory
     */
    public void setDest(File dest) {
        this.dest = dest;
    }

    private void validate() throws BuildException {
        if (source == null) {
            throw new BuildException("No Src specified", location);
        }

        if (!source.exists()) {
            throw new BuildException("Src doesn't exist", location);
        }

        if (source.isDirectory()) {
            throw new BuildException("Cannot expand a directory", location);
        }

        if (dest == null) {
            dest = new File(source.getParent());
        }

        if (dest.isDirectory()) {
            String defaultExtension = getDefaultExtension();
            createDestFile(defaultExtension);
        }
    }

    private void createDestFile(String defaultExtension) {
        String sourceName = source.getName();
        int len = sourceName.length();
        if (defaultExtension != null
            && len > defaultExtension.length()
            && defaultExtension.equalsIgnoreCase(sourceName.substring(len - defaultExtension.length()))) {
            dest = new File(dest, sourceName.substring(0,
                                                       len - defaultExtension.length()));
        } else {
            dest = new File(dest, sourceName);
        }
    }

    public void execute() throws BuildException {
        try {
            validate();
            extract();
        } finally {
            dest = savedDest;
        }
    }

    protected abstract String getDefaultExtension();
    protected abstract void extract();
}
