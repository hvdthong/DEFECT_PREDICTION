package org.apache.camel.model.loadbalancer;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.processor.loadbalancer.LoadBalancer;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;roundRobin/&gt; element
 */
@XmlRootElement(name = "roundRobin")
public class RoundRobinLoadBalanceStrategy extends LoadBalancerType {

    @Override
    protected LoadBalancer createLoadBalancer(RouteContext routeContext) {
        return new org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer();
    }

}
