package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.spi.Policy;
import org.apache.camel.util.ServiceHelper;

/**
 * A Delegate pattern which delegates processing to a nested processor which can
 * be useful for implementation inheritance when writing an {@link Policy}
 * 
 * @version $Revision: 658240 $
 */
public class DelegateProcessor extends ServiceSupport implements Processor {
    protected Processor processor;

    public DelegateProcessor() {
    }

    public DelegateProcessor(Processor processor) {
        if (processor == this) {
            throw new IllegalArgumentException("Recursive DelegateProcessor!");
        }
        this.processor = processor;
    }

    public void process(Exchange exchange) throws Exception {
        processNext(exchange);
    }

    protected void processNext(Exchange exchange) throws Exception {
        if (processor != null) {
            processor.process(exchange);
        }
    }

    @Override
    public String toString() {
        return "Delegate(" + processor + ")";
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(processor);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(processor);
    }

    /**
     * Proceed with the underlying delegated processor
     */
    public void proceed(Exchange exchange) throws Exception {
        processNext(exchange);
    }
}
