package org.apache.camel.builder;

import org.apache.camel.Processor;

/**
 * @version $Revision: 563607 $
 */
public class ConstantProcessorBuilder implements ProcessorFactory {
    private Processor processor;

    public ConstantProcessorBuilder(Processor processor) {
        this.processor = processor;
    }

    public Processor createProcessor() {
        return processor;
    }
}
