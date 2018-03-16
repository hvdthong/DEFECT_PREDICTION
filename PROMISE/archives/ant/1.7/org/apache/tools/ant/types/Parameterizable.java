package org.apache.tools.ant.types;

/**
 * Parameterizable objects take genric key value pairs.
 *
 */
public interface Parameterizable {
    /**
     * Set the parameters
     *
     * @param parameters an array of name/type/value parameters.
     */
    void setParameters(Parameter[] parameters);
}
