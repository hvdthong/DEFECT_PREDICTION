package org.apache.tools.ant.taskdefs;



/**
 *
 * Adds a data type definition to the current project.
 * Two attributes are
 * needed, the name that identifies this data type uniquely, and the full
 * name of the class (including the packages) that implements this
 * type.
 * <p>You can also define a group of data types at once using the file or
 * resource attributes.  These attributes point to files in the format of
 * Java property files.   Each line defines a single data type in the
 * format:</p>
 * <pre>
 * typename=fully.qualified.java.classname
 * </pre>
 * <p>Typedef should be used to add your own types to the system. Data
 * types are things likepaths or filesets that can be defined at
 * the project level and referenced via their ID attribute.</p>
 * <p>Custom data types usually need custom tasks to put them to good use.</p>
 *
 * @since Ant 1.4
 * @ant.task category="internal"
 */
public class Typedef extends Definer {
}
