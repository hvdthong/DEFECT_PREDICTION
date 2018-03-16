package org.apache.xerces.framework;

/**
 * This class defines the version number of the parser.
 *
 * @version
 */
public class Version {


    /** Version string. */
    public static String  fVersion = "Xerces 1.4.4";


    /**
     * Prints out the version number to System.out. This is needed
     * for the build system.
     */
    public static void main(String argv[]) {
        System.out.println(fVersion);
    }

