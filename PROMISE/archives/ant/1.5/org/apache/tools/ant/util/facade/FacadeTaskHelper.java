package org.apache.tools.ant.util.facade;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Helper class for facade implementations - encapsulates treatment of
 * explicit implementation choices, magic properties and
 * implementation specific command line arguments.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @version $Revision: 274041 $
 *
 * @since Ant 1.5
 */
public class FacadeTaskHelper {

    /**
     * Command line arguments.
     */
    private Vector args = new Vector();

    /**
     * The explicitly chosen implementation.
     */
    private String userChoice;

    /**
     * The magic property to consult.
     */
    private String magicValue;

    /**
     * The default value.
     */
    private String defaultValue;

    /**
     * @param defaultValue The default value for the implementation.
     * Must not be null.
     */
    public FacadeTaskHelper(String defaultValue) {
        this(defaultValue, null);
    }

    /**
     * @param defaultValue The default value for the implementation.
     * Must not be null.
     * @param magic the value of a magic property that may hold a user
     * choice.  May be null.
     */
    public FacadeTaskHelper(String defaultValue, String magicValue) {
        this.defaultValue = defaultValue;
        this.magicValue = magicValue;
    }

    /**
     * Used to set the value of the magic property.
     */
    public void setMagicValue(String magicValue) {
        this.magicValue = magicValue;
    }

    /**
     * Used for explicit user choices.
     */
    public void setImplementation(String userChoice) {
        this.userChoice = userChoice;
    }

    /**
     * Retrieves the implementation.
     */
    public String getImplementation() {
        return userChoice != null ? userChoice
                                  : (magicValue != null ? magicValue
                                                        : defaultValue);
    }

    /**
     * Retrieves the explicit user choice
     */
    public String getExplicitChoice() {
        return userChoice;
    }

    /**
     * Command line argument.
     */
    public void addImplementationArgument(ImplementationSpecificArgument arg) {
        args.addElement(arg);
    }

    /**
     * Retrieves the command line arguments enabled for the current
     * facade implementation.
     */
    public String[] getArgs() {
        Vector tmp = new Vector(args.size());
        for (Enumeration enum = args.elements(); enum.hasMoreElements();) {
            ImplementationSpecificArgument arg = 
                ((ImplementationSpecificArgument) enum.nextElement());
            String[] curr = arg.getParts(getImplementation());
            for (int i = 0; i < curr.length; i++) {
                tmp.addElement(curr[i]);
            }
        }
        String[] res = new String[tmp.size()];
        tmp.copyInto(res);
        return res;
    }

    /**
     * Tests whether the implementation has been chosen by the user
     * (either via a magic property or explicitly.
     *
     * @since Ant 1.5.2
     */
    public boolean hasBeenSet() {
        return userChoice != null || magicValue != null;
    }
}
