package org.apache.tools.ant.taskdefs.optional.extension.resolvers;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.extension.Extension;
import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;

/**
 * Resolver that just returns s specified location.
 *
 */
public class LocationResolver implements ExtensionResolver {
    private String location;

    /**
     * Sets the location for this resolver
     * @param location the location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Returns the resolved file
     * @param extension the extension
     * @param project the project
     * @return the file resolved
     * @throws BuildException if no location is set
     */
    public File resolve(final Extension extension,
                        final Project project) throws BuildException {
        if (null == location) {
            final String message = "No location specified for resolver";
            throw new BuildException(message);
        }

        return project.resolveFile(location);
    }
    /**
     * Returns a string representation of the Location
     * @return the string representation
     */
    public String toString() {
        return "Location[" + location + "]";
    }
}
