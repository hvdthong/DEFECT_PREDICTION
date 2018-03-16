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
 * @version $Revision: 727988 $
 */
@XmlRootElement(name = "multicast")
@XmlAccessorType(XmlAccessType.FIELD)
public class MulticastType extends OutputType<ProcessorType> {
    @XmlAttribute(required = false)
    private Boolean parallelProcessing;
    @XmlAttribute(required = false)
    private String strategyRef;
    @XmlAttribute(required = false)
    private String threadPoolRef;    
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

    /**
     * Set the multicasting aggregationStrategy
     * @param aggregationStrategy 
     *
     * @return the builder
     */
    public MulticastType aggregationStrategy(AggregationStrategy aggregationStrategy) {
        setAggregationStrategy(aggregationStrategy);
        return this;
    }
    
    /**
     * Set to run the multicasting action parallely
     *
     * @return the builder
     */
    public MulticastType parallelProcessing() {
        setParallelProcessing(true);
        return this;
    }
    
    /**
     * Set the multicasting action's thread model
     * @param parallelProcessing <tt>true</tt> to use a thread pool, 
     * if <tt>false</tt> then work is done in the calling thread
     *
     * @return the builder
     */
    public MulticastType parallelProcessing(boolean parallelProcessing) {
        setParallelProcessing(parallelProcessing);
        return this;
    }
    
    /**
     * Setting the executor for executing the multicasting action.
     *   
     * @param executor , it should be a instance of ThreadPoolExcutor
     * NOTE in Camel 2.0 , it will change to use the instance which implements Executor interface
     *
     * @return the builder
     */
    public MulticastType executor(ThreadPoolExecutor executor) {
        setThreadPoolExecutor(executor);
        return this;
    }    
        
    protected Processor createCompositeProcessor(RouteContext routeContext, List<Processor> list) {
        if (aggregationStrategy == null && strategyRef != null) {
            aggregationStrategy = routeContext.lookup(strategyRef, AggregationStrategy.class);
        }
        if (aggregationStrategy == null) {
            aggregationStrategy = new UseLatestAggregationStrategy();
        }
        if (threadPoolRef != null) {
            threadPoolExecutor = routeContext.lookup(threadPoolRef, ThreadPoolExecutor.class);
        }
        return new MulticastProcessor(list, aggregationStrategy, isParallelProcessing(), threadPoolExecutor);
    }

    public AggregationStrategy getAggregationStrategy() {
        return aggregationStrategy;
    }
    
    public void setAggregationStrategy(AggregationStrategy aggregationStrategy) {
        this.aggregationStrategy = aggregationStrategy;        
    }

    public boolean isParallelProcessing() {
        return parallelProcessing != null ? parallelProcessing : false;
    }

    public void setParallelProcessing(boolean parallelProcessing) {
        this.parallelProcessing = parallelProcessing;        
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor executor) {
        this.threadPoolExecutor = executor;        

    }
    
    @Override
    protected Processor wrapProcessorInInterceptors(RouteContext routeContext, Processor target) throws Exception {        
        return super.wrapProcessorInInterceptors(routeContext, new StreamCachingInterceptor(target));        
    }
}
