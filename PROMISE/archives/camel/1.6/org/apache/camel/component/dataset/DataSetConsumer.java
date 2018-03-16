package org.apache.camel.component.dataset;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.processor.ThroughputLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DataSet consumer.
 *
 * @version $Revision: 670452 $
 */
public class DataSetConsumer extends DefaultConsumer<Exchange> {
    private static final transient Log LOG = LogFactory.getLog(DataSetConsumer.class);
    private DataSetEndpoint endpoint;
    private Processor reporter;

    public DataSetConsumer(DataSetEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        if (reporter == null) {
            reporter = createReporter();
        }
        final DataSet dataSet = endpoint.getDataSet();
        final long preloadSize = endpoint.getPreloadSize();

        sendMessages(0, preloadSize);

        endpoint.getExecutorService().execute(new Runnable() {
            public void run() {
                sendMessages(preloadSize, dataSet.getSize());
            }
        });
    }

    protected void sendMessages(long startIndex, long endIndex) {
        try {
            for (long i = startIndex; i < endIndex; i++) {
                Exchange exchange = endpoint.createExchange(i);
                getProcessor().process(exchange);

                try {
                    long delay = endpoint.getProduceDelay();
                    if (delay < 3) {
                        delay = 3;
                    }
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    LOG.debug(e);
                }
                if (reporter != null) {
                    reporter.process(exchange);
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    protected ThroughputLogger createReporter() {
        ThroughputLogger answer = new ThroughputLogger(endpoint.getEndpointUri(), (int) endpoint.getDataSet().getReportCount());
        answer.setAction("Sent");
        return answer;
    }
}
