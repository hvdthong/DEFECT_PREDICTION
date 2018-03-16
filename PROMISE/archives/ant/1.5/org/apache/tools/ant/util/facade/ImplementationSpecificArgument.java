package org.apache.tools.ant.util.facade;

import org.apache.tools.ant.types.Commandline;

/**
 * Extension of Commandline.Argument with a new attribute that choses
 * a specific implementation of the facade.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @version $Revision: 274041 $
 *
 * @since Ant 1.5
 */
public class ImplementationSpecificArgument extends Commandline.Argument {
    private String impl;

    public ImplementationSpecificArgument() {
        super();
    }

    public void setImplementation(String impl) {
        this.impl = impl;
    }

    public final String[] getParts(String chosenImpl) {
        if (impl == null || impl.equals(chosenImpl)) {
            return super.getParts();
        } else {
            return new String[0];
        }
    }
}
