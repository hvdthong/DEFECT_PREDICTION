package org.apache.camel.model.loadbalancer;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.processor.loadbalancer.LoadBalancer;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;topic/&gt; element
 * 
 */
@XmlRootElement(name = "topic")
public class TopicLoadBalanceStrategy extends LoadBalancerType {
    @Override
    protected LoadBalancer createLoadBalancer(RouteContext routeContext) {
        return new org.apache.camel.processor.loadbalancer.TopicLoadBalancer();
    }

}
