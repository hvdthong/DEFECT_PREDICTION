package org.apache.ivy.core.settings;


/**
 * Store and provide access to the ivy variables.
 * 
 * @author gscokart
 */
public interface IvyVariableContainer extends Cloneable {

    public void setVariable(String varName, String value, boolean overwrite);

    public String getVariable(String name);

    /**
     * Specifies the prefix used to indicate a variable is an environment
     * variable. If the prefix doesn't end with a '.', it will be added
     * automatically.
     * 
     * @param prefix the prefix to use for the environment variables
     */
    public void setEnvironmentPrefix(String prefix);
    
    public Object clone();
}
