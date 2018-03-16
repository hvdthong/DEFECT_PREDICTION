package org.apache.camel.component.dataset;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.util.ExchangeHelper;

/**
 * Base class for DataSet
 *
 * @version $Revision: 659771 $
 */
public abstract class DataSetSupport implements DataSet {
    private Map<String, Object> defaultHeaders;
    private Processor outputTransformer;
    private long size = 10;
    private long reportCount = -1;

    public DataSetSupport() {
    }

    public DataSetSupport(int size) {
        setSize(size);
    }

    public void populateMessage(Exchange exchange, long messageIndex) throws Exception {
        Message in = exchange.getIn();
        in.setBody(createMessageBody(messageIndex));
        in.setHeaders(getDefaultHeaders());
        applyHeaders(exchange, messageIndex);

        if (outputTransformer != null) {
            outputTransformer.process(exchange);
        }
    }

    public void assertMessageExpected(DataSetEndpoint dataSetEndpoint, Exchange expected, Exchange actual, long index) throws Exception {
        Object expectedBody = expected.getIn().getBody();
        Object actualBody = actual.getIn().getBody();
        if (expectedBody != null) {
            actualBody = ExchangeHelper.getMandatoryInBody(actual, expectedBody.getClass());
        }
        DataSetEndpoint.assertEquals("message body", expectedBody, actualBody, actual);
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getReportCount() {
        if (reportCount <= 0) {
            reportCount = getSize() / 5;
        }
        return reportCount;
    }

    /**
     * Sets the number of messages in a group on which we will report that messages have been received.
     */
    public void setReportCount(long reportCount) {
        this.reportCount = reportCount;
    }

    public Map<String, Object> getDefaultHeaders() {
        if (defaultHeaders == null) {
            defaultHeaders = new HashMap<String, Object>();
            populateDefaultHeaders(defaultHeaders);
        }
        return defaultHeaders;
    }

    public void setDefaultHeaders(Map<String, Object> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public Processor getOutputTransformer() {
        return outputTransformer;
    }

    public void setOutputTransformer(Processor outputTransformer) {
        this.outputTransformer = outputTransformer;
    }


    protected abstract Object createMessageBody(long messageIndex);

    /**
     * Allows derived classes to add some custom headers for a given message
     */
    protected void applyHeaders(Exchange exchange, long messageIndex) {
    }

    /**
     * Allows derived classes to customize a default set of properties
     */
    protected void populateDefaultHeaders(Map<String, Object> map) {
    }
}
