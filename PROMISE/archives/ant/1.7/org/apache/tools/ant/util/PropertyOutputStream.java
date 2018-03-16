package org.apache.tools.ant.util;

import java.io.ByteArrayOutputStream;

import org.apache.tools.ant.Project;

/**
 * Exception thrown when an attempt is made to get an OutputStream
 * from an immutable Resource.
 * @since Ant 1.7
 */
public class PropertyOutputStream extends ByteArrayOutputStream {
    private Project project;
    private String property;
    private boolean trim;

    /**
     * Construct a new PropertyOutputStream for the specified Project
     * and property name, trimming the property value.
     * @param p the associated Ant Project.
     * @param s the String property name.
     */
    public PropertyOutputStream(Project p, String s) {
        this(p, s, true);
    }

    /**
     * Construct a new PropertyOutputStream for
     * the specified Project, property name, and trim mode.
     * @param p the associated Ant Project.
     * @param s the String property name.
     * @param b the boolean trim mode.
     */
    public PropertyOutputStream(Project p, String s, boolean b) {
        project = p;
        property = s;
        trim = b;
    }

    /**
     * Close the PropertyOutputStream, storing the property.
     */
    public void close() {
        if (project != null && property != null) {
            String s = new String(toByteArray());
            project.setNewProperty(property, trim ? s.trim() : s);
        }
    }

}

