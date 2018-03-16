package org.apache.camel.processor;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.spi.Policy;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * A Delegate pattern which delegates processing to a nested processor which can be useful for implementation inheritence
 * when writing an {@link Policy}
 *
 * @version $Revision: 519941 $
 */
public class DelegateProcessor extends ServiceSupport implements Processor {
    protected Processor next;

    public DelegateProcessor() {
    }

    public DelegateProcessor(Processor next) {
        this.next = next;
    }

    public void process(Exchange exchange) throws Exception {
        processNext(exchange);
    }

    protected void processNext(Exchange exchange) throws Exception {
        if (next != null) {
            next.process(exchange);
        }
    }

    @Override
    public String toString() {
        return "delegate(" + next + ")";
    }

    public Processor getNext() {
        return next;
    }

    public void setNext(Processor next) {
        this.next = next;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(next);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(next);
    }
}
