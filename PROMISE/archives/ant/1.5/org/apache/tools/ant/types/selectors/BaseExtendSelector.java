package org.apache.tools.ant.types.selectors;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Parameter;


/**
 * Convenience base class for all selectors accessed through ExtendSelector.
 * It provides support for gathering the parameters together as well as for
 * assigning an error message and throwing a build exception if an error is
 * detected.
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public abstract class BaseExtendSelector extends BaseSelector implements
        ExtendFileSelector {

    /** The passed in parameter array. */
    protected Parameter[] parameters = null;

    /**
     * Default constructor.
     */
    public BaseExtendSelector() {
    }

    /**
     * Set all the Parameters for this custom selector, collected by
     * the ExtendSelector class.
     *
     * @param parameters the complete set of parameters for this selector
     */
    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Allows access to the parameters gathered and set within the
     * &lt;custom&gt; tag.
     *
     * @return the set of parameters defined for this selector
     */
    protected Parameter[] getParameters() {
        return parameters;
    }

    /**
     * Method that each selector will implement to create their
     * selection behaviour. If there is a problem with the setup
     * of a selector, it can throw a BuildException to indicate
     * the problem.
     *
     * @param basedir A java.io.File object for the base directory
     * @param filename The name of the file to check
     * @param file A File object for this filename
     * @return whether the file should be selected or not
     */
    public abstract boolean isSelected(File basedir, String filename,
                                       File file)
            throws BuildException;

}

