package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.util.ObjectHelper;

/**
 * A simple processor which just sends the message exchange to the default endpoint of the {@link ProducerTemplate}
 *
 * @version $Revision: 724293 $
 */
public class ProducerTemplateProcessor implements Processor {
    private final ProducerTemplate producerTemplate;

    public ProducerTemplateProcessor(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
        ObjectHelper.notNull(producerTemplate,  "producerTemplate");
    }

    public void process(Exchange exchange) throws Exception {
        producerTemplate.send(exchange);
    }

    @Override
    public String toString() {
        return "ProducerTemplateProcessor[" + producerTemplate + "]";
    }
}
