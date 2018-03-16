package org.apache.camel.model;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.loadbalancer.LoadBalancerType;
import org.apache.camel.model.loadbalancer.RandomLoadBalanceStrategy;
import org.apache.camel.model.loadbalancer.RoundRobinLoadBalanceStrategy;
import org.apache.camel.model.loadbalancer.StickyLoadBalanceStrategy;
import org.apache.camel.model.loadbalancer.TopicLoadBalanceStrategy;
import org.apache.camel.processor.SendProcessor;
import org.apache.camel.processor.loadbalancer.LoadBalancer;
import org.apache.camel.processor.loadbalancer.RandomLoadBalancer;
import org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer;
import org.apache.camel.processor.loadbalancer.StickyLoadBalancer;
import org.apache.camel.processor.loadbalancer.TopicLoadBalancer;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.CollectionStringBuffer;

/**
 * Represents an XML &lt;loadBalance/&gt; element
 */
@XmlRootElement(name = "loadBalance")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoadBalanceType extends OutputType<LoadBalanceType> {
    @XmlAttribute(required = false)
    private String ref;

    @XmlElements({
        @XmlElement(required = false, name = "roundRobin", type = RoundRobinLoadBalanceStrategy.class),
        @XmlElement(required = false, name = "random", type = RandomLoadBalanceStrategy.class),
        @XmlElement(required = false, name = "sticky", type = StickyLoadBalanceStrategy.class),
        @XmlElement(required = false, name = "topic", type = TopicLoadBalanceStrategy.class)}
        )
    private LoadBalancerType loadBalancerType;

    public LoadBalanceType() {
    }

    @Override
    public String getShortName() {
        return "loadbalance";
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public LoadBalancerType getLoadBalancerType() {
        return loadBalancerType;
    }

    public void setLoadBalancerType(LoadBalancerType loadbalancer) {
        loadBalancerType = loadbalancer;
    }

    protected Processor createOutputsProcessor(RouteContext routeContext, Collection<ProcessorType<?>> outputs)
        throws Exception {
        LoadBalancer loadBalancer = LoadBalancerType.getLoadBalancer(routeContext, loadBalancerType, ref);
        for (ProcessorType processorType : outputs) {
            SendProcessor processor = (SendProcessor) processorType.createProcessor(routeContext);
            loadBalancer.addProcessor(processor);
        }
        return loadBalancer;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        LoadBalancer loadBalancer = LoadBalancerType.getLoadBalancer(routeContext, loadBalancerType, ref);
        for (ProcessorType processorType : getOutputs()) {
            SendProcessor processor = (SendProcessor) processorType.createProcessor(routeContext);
            loadBalancer.addProcessor(processor);
        }

        return loadBalancer;
    }

    public LoadBalanceType setLoadBalancer(LoadBalancer loadBalancer) {
        loadBalancerType = new LoadBalancerType(loadBalancer);
        return this;
    }

    public LoadBalanceType roundRobin() {
        loadBalancerType = new LoadBalancerType(new RoundRobinLoadBalancer());
        return this;
    }

    public LoadBalanceType random() {
        loadBalancerType = new LoadBalancerType(new RandomLoadBalancer());
        return this;
    }

    public LoadBalanceType sticky(Expression<Exchange> correlationExpression) {
        loadBalancerType = new LoadBalancerType(new StickyLoadBalancer(correlationExpression));
        return this;
    }

    public LoadBalanceType topic() {
        loadBalancerType = new LoadBalancerType(new TopicLoadBalancer());
        return this;
    }

    @Override
    public String getLabel() {
        CollectionStringBuffer buffer = new CollectionStringBuffer();
        List<ProcessorType<?>> list = getOutputs();
        for (ProcessorType<?> processorType : list) {
            buffer.append(processorType.getLabel());
        }
        return buffer.toString();
    }

    @Override
    public String toString() {
        String result;
        if (loadBalancerType != null) {
            result = "LoadBalanceType[" + loadBalancerType + ", ";
        } else {
            result =  "LoadBalanceType[" + ref + ", ";
        }
        result = result + getOutputs() + "]";
        return result;
    }

}
