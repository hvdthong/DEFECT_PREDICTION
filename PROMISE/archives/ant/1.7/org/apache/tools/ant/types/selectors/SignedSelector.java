package org.apache.tools.ant.types.selectors;
import java.io.File;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.taskdefs.condition.IsSigned;

/**
 * Selector that chooses files based on whether they are signed or not.
 *
 * @since 1.7
 */
public class SignedSelector extends DataType implements FileSelector {
    private IsSigned isSigned = new IsSigned();

    /**
     * The signature name to check jarfile for.
     *
     * @param name signature to look for.
     */
    public void setName(String name) {
        isSigned.setName(name);
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a file in a particular fileset.
     *
     * @param basedir not used by this selector
     * @param filename not used by this selector
     * @param file     path to file to be selected
     * @return whether the file should be selected or not
     */
    public boolean isSelected(File basedir, String filename, File file) {
        if (file.isDirectory()) {
        }
        isSigned.setProject(getProject());
        isSigned.setFile(file);
        return isSigned.eval();
    }
}
