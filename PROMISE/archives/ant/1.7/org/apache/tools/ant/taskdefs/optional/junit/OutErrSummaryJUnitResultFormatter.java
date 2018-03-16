package org.apache.tools.ant.taskdefs.optional.junit;

/**
 * Used instead of SummaryJUnitResultFormatter in forked tests if
 * withOutAndErr is requested.
 */

public class OutErrSummaryJUnitResultFormatter
    extends SummaryJUnitResultFormatter {

    /**
     * Empty
     */
    public OutErrSummaryJUnitResultFormatter() {
        super();
        setWithOutAndErr(true);
    }
}
