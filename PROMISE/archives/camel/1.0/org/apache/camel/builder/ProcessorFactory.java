package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * An interface representing a builder of a {@link Processor}
 * 
 * @version $Revision: 534145 $
 */
public interface ProcessorFactory {

    public Processor createProcessor() throws Exception;

}
