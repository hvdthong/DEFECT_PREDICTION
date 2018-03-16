package org.apache.tools.ant.taskdefs;

/**
 * Define a new data type.
 *
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Typedef extends Definer {
    protected void addDefinition(String name, Class c) {
        project.addDataTypeDefinition(name, c);
    }
}
