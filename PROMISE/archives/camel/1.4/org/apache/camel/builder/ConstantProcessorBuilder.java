package org.apache.camel.builder;

import org.apache.camel.Processor;

/**
 * A builder wrapping a {@link Processor}.
 *
 * @version $Revision: 659760 $
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
