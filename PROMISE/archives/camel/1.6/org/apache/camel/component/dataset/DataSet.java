package org.apache.camel.component.dataset;

import org.apache.camel.Exchange;

/**
 * Represents a strategy for testing endpoints with canned data.
 *
 * @version $Revision: 660275 $
 */
public interface DataSet {

    String INDEX_HEADER = "camelDataSetIndex";

    /**
     * Populates a message exchange when using the DataSet as a source of messages
     *
     * @param exchange
     */
    void populateMessage(Exchange exchange, long messageIndex) throws Exception;

    /**
     * Returns the size of the dataset
     */
    long getSize();

    /**
     * Asserts that the expected message has been received for the given index
     */
    void assertMessageExpected(DataSetEndpoint dataSetEndpoint, Exchange expected, Exchange actual, long index) throws Exception;

    /**
     * Returns the number of messages which should be received before reporting on the progress of the test
     */
    long getReportCount();
}
