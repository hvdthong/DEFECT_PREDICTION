package org.apache.camel.management;

import java.util.Map;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.InterceptStrategy;

/**
 * This strategy class wraps targeted processors with a
 * {@link InstrumentationProcessor}. Each InstrumentationProcessor has an
 * embedded {@link PerformanceCounter} for monitoring performance metrics.
 * <p/>
 * This class looks up a map to determine which PerformanceCounter should go into the
 * InstrumentationProcessor for any particular target processor.
 *
 * @version $Revision: 671035 $
 */
public class InstrumentationInterceptStrategy implements InterceptStrategy {

    private Map<ProcessorType, PerformanceCounter> counterMap;

    public InstrumentationInterceptStrategy(Map<ProcessorType, PerformanceCounter> counterMap) {
        this.counterMap = counterMap;
    }

    public Processor wrapProcessorInInterceptors(ProcessorType processorType,
            Processor target) throws Exception {

        Processor retval = target;
        PerformanceCounter counter = counterMap.get(processorType);

        if (counter != null) {
            InstrumentationProcessor wrapper = new InstrumentationProcessor(counter);
            wrapper.setProcessor(target);
            retval = wrapper;
        }

        return retval;
    }

}
