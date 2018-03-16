package org.apache.tools.ant.taskdefs.optional.extension.resolvers;

import java.io.File;
import java.net.URL;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Get;
import org.apache.tools.ant.taskdefs.optional.extension.Extension;
import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;

/**
 * Resolver that just returns s specified location.
 *
 */
public class URLResolver implements ExtensionResolver {
    private File destfile;
    private File destdir;
    private URL url;

    /**
     * Sets the URL
     * @param url the url
     */
    public void setUrl(final URL url) {
        this.url = url;
    }

    /**
     * Sets the destination file
     * @param destfile the destination file
     */
    public void setDestfile(final File destfile) {
        this.destfile = destfile;
    }

    /**
     * Sets the destination directory
     * @param destdir the destination directory
     */
    public void setDestdir(final File destdir) {
        this.destdir = destdir;
    }

    /**
     * Returns the file resolved from URL and directory
     * @param extension the extention
     * @param project the project
     * @return file the file resolved
     * @throws BuildException if the URL is invalid
     */
    public File resolve(final Extension extension,
                         final Project project) throws BuildException {
        validate();

        final File file = getDest();

        final Get get = (Get) project.createTask("get");
        get.setDest(file);
        get.setSrc(url);
        get.execute();

        return file;
    }

    /*
     * Gets the destination file
     */
    private File getDest() {
        File result;
        if (null != destfile) {
            result = destfile;
        } else {
            final String file = url.getFile();
            String filename;
            if (null == file || file.length() <= 1) {
                filename = "default.file";
            } else {
                int index = file.lastIndexOf('/');
                if (-1 == index) {
                    index = 0;
                }
                filename = file.substring(index);
            }
            result = new File(destdir, filename);
        }
        return result;
    }

    /*
     * Validates URL
     */
    private void validate() {
        if (null == url) {
            final String message = "Must specify URL";
            throw new BuildException(message);
        }

        if (null == destdir && null == destfile) {
            final String message = "Must specify destination file or directory";
            throw new BuildException(message);
        } else if (null != destdir && null != destfile) {
            final String message = "Must not specify both destination file or directory";
            throw new BuildException(message);
        }
    }

    /**
     * Returns a string representation of the URL
     * @return the string representation
     */
    public String toString() {
        return "URL[" + url + "]";
    }
}
