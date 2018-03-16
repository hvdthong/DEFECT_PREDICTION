package org.apache.tools.ant;

/**
 * Stores the file name and line number in a file.
 */
public class Location {
    private String fileName;
    private int lineNumber;
    private int columnNumber;

    public static final Location UNKNOWN_LOCATION = new Location();

    /**
     * Creates an "unknown" location.
     */
    private Location() {
        this(null, 0, 0);
    }

    /**
     * Creates a location consisting of a file name but no line number.
     */
    public Location(String fileName) {
        this(fileName, 0, 0);
    }

    /**
     * Creates a location consisting of a file name and line number.
     */
    public Location(String fileName, int lineNumber, int columnNumber) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * Returns the file name, line number and a trailing space. An error
     * message can be appended easily. For unknown locations, returns
     * an empty string.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();

        if (fileName != null) {
            buf.append(fileName);

            if (lineNumber != 0) {
                buf.append(":");
                buf.append(lineNumber);
            }

            buf.append(": ");
        }

        return buf.toString();
    }
}
