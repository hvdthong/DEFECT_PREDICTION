package org.apache.camel.builder;

import org.apache.camel.Processor;

/**
 * An interface representing a builder of a {@link Processor}
 *
 * @version $Revision: 563607 $
 */
public interface ProcessorFactory {

    Processor createProcessor() throws Exception;

}
