package org.apache.tools.ant.taskdefs.optional.extension;

/**
 * Enum used in (@link Extension) to indicate the compatability
 * of one extension to another. See (@link Extension) for instances
 * of object.
 *
 * WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
 *  This file is from excalibur.extension package. Dont edit this file
 * directly as there is no unit tests to make sure it is operational
 * in ant. Edit file in excalibur and run tests there before changing
 * ants file.
 * WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
 *
 * @see Extension
 */
public final class Compatability {
    /**
     * A string representaiton of compatability level.
     */
    private final String name;

    /**
     * Create a compatability enum with specified name.
     *
     * @param name the name of compatability level
     */
    Compatability(final String name) {
        this.name = name;
    }

    /**
     * Return name of compatability level.
     *
     * @return the name of compatability level
     */
    public String toString() {
        return name;
    }
}
