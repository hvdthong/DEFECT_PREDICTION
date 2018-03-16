package org.apache.synapse.mediators.eip;

/** Constants related to the EIP mediators */
public final class EIPConstants {

    /** Typically the message ID of the parent message in a split/iterate etc so that
     * its children could be uniquely aggregated by the aggrgate mediator etc
     */
    public static final String AGGREGATE_CORRELATION = "aggregateCorelation";

    /** Constant for the message sequence property key */
    public static final String MESSAGE_SEQUENCE = "messageSequence";

    /** Delimiter for the message sequence value */
    public static final String MESSAGE_SEQUENCE_DELEMITER = "/";
}
