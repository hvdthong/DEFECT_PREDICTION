package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

/**
 * EnumeratedAttribute for time comparisons.  Accepts values
 * "before", "after", "equal".
 * @since Ant 1.7
 */
public class TimeComparison extends EnumeratedAttribute {
    private static final String[] VALUES
        = new String[] {"before", "after", "equal"};

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /** Before Comparison. */
    public static final TimeComparison BEFORE = new TimeComparison("before");

    /** After Comparison. */
    public static final TimeComparison AFTER = new TimeComparison("after");

    /** Equal Comparison. */
    public static final TimeComparison EQUAL = new TimeComparison("equal");

    /**
     * Default constructor.
     */
    public TimeComparison() {
    }

    /**
     * Construct a new TimeComparison with the specified value.
     * @param value the EnumeratedAttribute value.
     */
    public TimeComparison(String value) {
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
     * Evaluate two times against this TimeComparison.
     * @param t1 the first time to compare.
     * @param t2 the second time to compare.
     * @return true if the comparison result fell within the parameters of this TimeComparison.
     */
    public boolean evaluate(long t1, long t2) {
        return evaluate(t1, t2, FILE_UTILS.getFileTimestampGranularity());
    }

    /**
     * Evaluate two times against this TimeComparison.
     * @param t1 the first time to compare.
     * @param t2 the second time to compare.
     * @param g the timestamp granularity.
     * @return true if the comparison result fell within the parameters of this TimeComparison.
     */
    public boolean evaluate(long t1, long t2, long g) {
        int cmp = getIndex();
        if (cmp == -1) {
            throw new BuildException("TimeComparison value not set.");
        }
        if (cmp == 0) {
            return t1 - g < t2;
        }
        if (cmp == 1) {
            return t1 + g > t2;
        }
        return Math.abs(t1 - t2) <= g;
    }

    /**
     * Compare two times.
     * @param t1 the first time to compare.
     * @param t2 the second time to compare.
     * @return a negative integer, a positive integer, or zero as t1 is
     *         before, after, or equal to t2 accounting for the default granularity.
     */
    public static int compare(long t1, long t2) {
        return compare(t1, t2, FILE_UTILS.getFileTimestampGranularity());
    }

    /**
     * Compare two times.
     * @param t1 the first time to compare.
     * @param t2 the second time to compare.
     * @param g the timestamp granularity.
     * @return a negative integer, a positive integer, or zero as t1 is
     *         before, after, or equal to t2 accounting for the specified granularity.
     */
    public static int compare(long t1, long t2, long g) {
        long diff = t1 - t2;
        long abs = Math.abs(diff);
        return abs > Math.abs(g) ? (int) (diff / abs) : 0;
    }

}

