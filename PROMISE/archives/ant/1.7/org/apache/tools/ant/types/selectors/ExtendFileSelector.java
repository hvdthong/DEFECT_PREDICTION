package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.Parameterizable;

/**
 * This is the interface to be used by all custom selectors, those that are
 * called through the &lt;custom&gt; tag. It is the amalgamation of two
 * interfaces, the FileSelector and the Paramterizable interface. Note that
 * you will almost certainly want the default behaviour for handling
 * Parameters, so you probably want to use the BaseExtendSelector class
 * as the base class for your custom selector rather than implementing
 * this interface from scratch.
 *
 * @since 1.5
 */
public interface ExtendFileSelector extends FileSelector, Parameterizable {

}

