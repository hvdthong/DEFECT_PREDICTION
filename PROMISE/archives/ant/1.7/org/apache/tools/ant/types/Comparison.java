package org.apache.tools.ant.types;

import java.util.Arrays;

import org.apache.tools.ant.BuildException;

/**
 * EnumeratedAttribute for generic comparisons.  Accepts values
 * "equal", "greater", "more", "less", "ne" (not equal),
 * "ge" (greater or equal), "le" (less or equal), "eq" (equal),
 * "gt" (greater), "lt" (less).
 * @since Ant 1.7
 */
public class Comparison extends EnumeratedAttribute {
    private static final String[] VALUES
        = new String[] {"equal", "greater", "less",
                        "ne", "ge", "le", "eq", "gt", "lt", "more"};

    /** Equal Comparison. */
    public static final Comparison EQUAL = new Comparison("equal");

    /** Not-Equal Comparison. */
    public static final Comparison NOT_EQUAL = new Comparison("ne");

    /** Greater Comparison. */
    public static final Comparison GREATER = new Comparison("greater");

    /** Less Comparison. */
    public static final Comparison LESS = new Comparison("less");

    /** Greater-or-Equal Comparison. */
    public static final Comparison GREATER_EQUAL = new Comparison("ge");

    /** Less-or-Equal Comparison. */
    public static final Comparison LESS_EQUAL = new Comparison("le");

    private static final int[] EQUAL_INDEX = {0, 4, 5, 6};
    private static final int[] LESS_INDEX = {2, 3, 5, 8};
    private static final int[] GREATER_INDEX = {1, 3, 4, 7, 9};

    /**
     * Default constructor.
     */
    public Comparison() {
    }

    /**
     * Construct a new Comparison with the specified value.
     * @param value the EnumeratedAttribute value.
     */
    public Comparison(String value) {
        setValue(value);
    }

    /**
     * Return the possible values.
     * @return String[] of EnumeratedAttribute values.
     */
    public String[] getValues() {
        return VALUES;
    }

    /**
     * Evaluate a comparison result as from Comparator.compare() or Comparable.compareTo().
     * @param comparisonResult the result to evaluate.
     * @return true if the comparison result fell within the parameters of this Comparison.
     */
    public boolean evaluate(int comparisonResult) {
        if (getIndex() == -1) {
            throw new BuildException("Comparison value not set.");
        }
        int[] i = comparisonResult < 0 ? LESS_INDEX
            : comparisonResult > 0 ? GREATER_INDEX : EQUAL_INDEX;
        return Arrays.binarySearch(i, getIndex()) >= 0;
    }

}

