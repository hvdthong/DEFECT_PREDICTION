package org.apache.tools.ant.types;

/**
 * Parameterizable objects take genric key value pairs.
 *
 * @author Magesh Umasankar
 */
public interface Parameterizable {
    void setParameters(Parameter[] parameters);
}
