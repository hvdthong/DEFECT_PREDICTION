package org.apache.camel.model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.Splitter;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.UseLatestAggregationStrategy;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;splitter/&gt; element
 *
 * @version $Revision: 727988 $
 */
@XmlRootElement(name = "splitter")
@XmlAccessorType(XmlAccessType.FIELD)
public class SplitterType extends ExpressionNode {
    @XmlTransient
    private AggregationStrategy aggregationStrategy;
    @XmlAttribute(required = false)
    private Boolean parallelProcessing;
    @XmlTransient
    private ThreadPoolExecutor threadPoolExecutor;
    @XmlAttribute(required = false)
    private String threadPoolExecutorRef;
    @XmlAttribute(required = false)
    private Boolean streaming = false;
    
    public SplitterType() {
    }

    public SplitterType(Expression expression) {
        super(expression);
    }

    public SplitterType(ExpressionType expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "Splitter[" + getExpression() + " -> " + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "splitter";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        if (aggregationStrategy == null) {
            aggregationStrategy = new UseLatestAggregationStrategy();
        }
        threadPoolExecutor = createThreadPoolExecutor(routeContext);
        return new Splitter(getExpression().createExpression(routeContext), childProcessor, aggregationStrategy,
                isParallelProcessing(), threadPoolExecutor, streaming);
    }
    
    /**
     * Set the spliter's aggregationStrategy
     * @param aggregationStrategy 
     *
     * @return the builder
     */
    public SplitterType aggregationStrategy(AggregationStrategy aggregationStrategy) {
        setAggregationStrategy(aggregationStrategy);
        return this;
    }
    
    /**
     * Set to run the splitting action parallelly
     * 
     * @return the builder
     */
    public SplitterType parallelProcessing() {
        setParallelProcessing(true);
        return this;
    }
    
    /**
     * Set the splitting action's thread model
     * @param parallelProcessing <tt>true</tt> to use a thread pool, 
     * if <tt>false</tt> then work is done in the calling thread. 
     *
     * @return the builder
     */
    public SplitterType parallelProcessing(boolean parallelProcessing) {
        setParallelProcessing(parallelProcessing);
        return this;
    }
    
    /**
     * Enables streaming. 
     * See {@link SplitterType#setStreaming(boolean)} for more information
     *
     * @return the builder
     */
    public SplitterType streaming() {
        setStreaming(true);
        return this;
    }
    
    /**
     * Setting the executor for executing the splitting action. 
     * @param executor , it should be a instance of ThreadPoolExcutor
     * NOTE in Camel 2.0 , it will change to use the instance which implements Executor interface
     * @return the builder
     */
    public SplitterType executor(ThreadPoolExecutor executor) {
        setThreadPoolExecutor(executor);
        return this;
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
    
    /**
     * The splitter should use streaming -- exchanges are being sent as the data for them becomes available.
     * This improves throughput and memory usage, but it has a drawback: 
     * - the sent exchanges will no longer contain the {@link Splitter#SPLIT_SIZE} header property 
     * 
     * @return whether or not streaming should be used
     */
    public boolean isStreaming() {
        return streaming != null ? streaming : false;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }


    private ThreadPoolExecutor createThreadPoolExecutor(RouteContext routeContext) {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        if (threadPoolExecutor == null && threadPoolExecutorRef != null) {
            threadPoolExecutor = routeContext.lookup(threadPoolExecutorRef, ThreadPoolExecutor.class);
        }
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(4, 16, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        }
        return threadPoolExecutor;
    }    
   
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}
