package org.apache.tools.ant.taskdefs.optional.extension;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Reference;

/**
 * The Extension set lists a set of "Optional Packages" /
 * "Extensions".
 *
 * @ant.datatype name="extension-set"
 */
public class ExtensionSet
    extends DataType {
    /**
     * ExtensionAdapter objects representing extensions.
     */
    private final ArrayList extensions = new ArrayList();

    /**
     * Filesets specifying all the extensions wanted.
     */
    private final ArrayList extensionsFilesets = new ArrayList();

    /**
     * Adds an extension that this library requires.
     *
     * @param extensionAdapter an extension that this library requires.
     */
    public void addExtension(final ExtensionAdapter extensionAdapter) {
        extensions.add(extensionAdapter);
    }

    /**
     * Adds a set of files about which extensions data will be extracted.
     *
     * @param fileSet a set of files about which extensions data will be extracted.
     */
    public void addLibfileset(final LibFileSet fileSet) {
        extensionsFilesets.add(fileSet);
    }

    /**
     * Adds a set of files about which extensions data will be extracted.
     *
     * @param fileSet a set of files about which extensions data will be extracted.
     */
    public void addFileset(final FileSet fileSet) {
        extensionsFilesets.add(fileSet);
    }

    /**
     * Extract a set of Extension objects from the ExtensionSet.
     *
     * @param proj the project instance.
     * @return an array containing the Extensions from this set
     * @throws BuildException if an error occurs
     */
    public Extension[] toExtensions(final Project proj)
        throws BuildException {
        final ArrayList extensionsList = ExtensionUtil.toExtensions(extensions);
        ExtensionUtil.extractExtensions(proj, extensionsList, extensionsFilesets);
        return (Extension[]) extensionsList.toArray(new Extension[extensionsList.size()]);
    }

    /**
     * Makes this instance in effect a reference to another ExtensionSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     *
     * @param reference the reference to which this instance is associated
     * @exception BuildException if this instance already has been configured.
     */
    public void setRefid(final Reference reference)
        throws BuildException {
        if (!extensions.isEmpty() || !extensionsFilesets.isEmpty()) {
            throw tooManyAttributes();
        }
        final Object object =
            reference.getReferencedObject(getProject());
        if (object instanceof ExtensionSet) {
            final ExtensionSet other = (ExtensionSet) object;
            extensions.addAll(other.extensions);
            extensionsFilesets.addAll(other.extensionsFilesets);
        } else {
            final String message =
                reference.getRefId() + " doesn\'t refer to a ExtensionSet";
            throw new BuildException(message);
        }

        super.setRefid(reference);
    }

    /**
     * @see java.lang.Object#toString()
     * @return the extensions in a string.
     */
    public String toString() {
        return "ExtensionSet" + Arrays.asList(toExtensions(getProject()));
    }
}
