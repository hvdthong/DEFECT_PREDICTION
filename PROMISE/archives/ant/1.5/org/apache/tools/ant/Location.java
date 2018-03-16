package org.apache.tools.ant;

/**
 * Stores the location of a piece of text within a file (file name,
 * line number and column number). Note that the column number is
 * currently ignored.
 */
public class Location {
    
    /** Name of the file. */
    private String fileName;
    /** Line number within the file. */
    private int lineNumber;
    /** Column number within the file. */
    private int columnNumber;

    /** Location to use when one is needed but no information is available */
    public static final Location UNKNOWN_LOCATION = new Location();

    /**
     * Creates an "unknown" location.
     */
    private Location() {
        this(null, 0, 0);
    }

    /**
     * Creates a location consisting of a file name but no line number or
     * column number.
     * 
     * @param fileName The name of the file. May be <code>null</code>,
     *                 in which case the location is equivalent to
     *                 {@link #UNKNOWN_LOCATION UNKNOWN_LOCATION}.
     */
    public Location(String fileName) {
        this(fileName, 0, 0);
    }

    /**
     * Creates a location consisting of a file name, line number and
     * column number.
     * 
     * @param fileName The name of the file. May be <code>null</code>,
     *                 in which case the location is equivalent to
     *                 {@link #UNKNOWN_LOCATION UNKNOWN_LOCATION}.
     * 
     * @param lineNumber Line number within the file. Use 0 for unknown
     *                   positions within a file.
     * @param columnNumber Column number within the line.
     */
    public Location(String fileName, int lineNumber, int columnNumber) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * Returns the file name, line number, a colon and a trailing space. 
     * An error message can be appended easily. For unknown locations, an 
     * empty string is returned.
     * 
     * @return a String of the form <code>"fileName: lineNumber: "</code>
     *         if both file name and line number are known,
     *         <code>"fileName: "</code> if only the file name is known,
     *         and the empty string for unknown locations.
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
