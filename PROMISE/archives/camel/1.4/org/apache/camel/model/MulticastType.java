package org.apache.camel.model;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Processor;
import org.apache.camel.processor.MulticastProcessor;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.processor.interceptor.StreamCachingInterceptor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;multicast/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "multicast")
@XmlAccessorType(XmlAccessType.FIELD)
public class MulticastType extends OutputType<ProcessorType> {
    @XmlAttribute(required = false)
    private Boolean parallelProcessing;
    @XmlTransient
    private AggregationStrategy aggregationStrategy;
    @XmlTransient
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public String toString() {
        return "Multicast[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "multicast";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return createOutputsProcessor(routeContext);
    }

    protected Processor createCompositeProcessor(List<Processor> list) {
        if (aggregationStrategy == null) {
            aggregationStrategy = new UseLatestAggregationStrategy();
        }
        return new MulticastProcessor(list, aggregationStrategy, isParallelProcessing(), threadPoolExecutor);
    }

    public AggregationStrategy getAggregationStrategy() {
        return aggregationStrategy;
    }

    public MulticastType setAggregationStrategy(AggregationStrategy aggregationStrategy) {
        this.aggregationStrategy = aggregationStrategy;
        return this;
    }

    public boolean isParallelProcessing() {
        return parallelProcessing != null ? parallelProcessing : false;
    }

    public MulticastType setParallelProcessing(boolean parallelProcessing) {
        this.parallelProcessing = parallelProcessing;
        return this;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public MulticastType setThreadPoolExecutor(ThreadPoolExecutor executor) {
        this.threadPoolExecutor = executor;
        return this;
    }

    @Override
    protected Processor wrapProcessorInInterceptors(RouteContext routeContext, Processor target) throws Exception {
        return new StreamCachingInterceptor(target);
    }
}
