package org.apache.tools.ant.taskdefs.condition;

import java.io.IOException;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.ResourceUtils;

/**
 * Compares resources for equality based on size and content.
 * All resources specified must match; no resource collections
 * specified is an error condition; if resource collections are
 * specified, but yield fewer than two elements, the condition
 * evaluates to <code>true</code>.
 * @since Ant 1.7
 */
public class ResourcesMatch implements Condition {

    private Union resources = null;
    private boolean asText = false;

    /**
     * Set whether to treat resources as if they were text files,
     * ignoring line endings.
     * @param asText whether to ignore line endings.
     */
    public void setAsText(boolean asText) {
        this.asText = asText;
    }

    /**
     * Add a resource collection.
     * @param rc the resource collection to add.
     */
    public void add(ResourceCollection rc) {
        if (rc == null) {
            return;
        }
        resources = resources == null ? new Union() : resources;
        resources.add(rc);
    }

    /**
     * Verify that all resources match.
     * @return true if all resources are equal.
     * @exception BuildException if there is an error.
     */
    public boolean eval() throws BuildException {
        if (resources == null) {
            throw new BuildException(
                "You must specify one or more nested resource collections");
        }
        if (resources.size() > 1) {
            Iterator i = resources.iterator();
            Resource r1 = (Resource) i.next();
            Resource r2 = null;

            while (i.hasNext()) {
                r2 = (Resource) i.next();
                try {
                    if (!ResourceUtils.contentEquals(r1, r2, asText)) {
                        return false;
                    }
                } catch (IOException ioe) {
                    throw new BuildException("when comparing resources "
                        + r1.toString() + " and " + r2.toString(), ioe);
                }
                r1 = r2;
            }
        }
        return true;
    }
}
