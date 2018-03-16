package org.apache.camel.model.loadbalancer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.loadbalancer.LoadBalancer;

/**
 * Represents an XML &lt;sticky/&gt; element
 */
@XmlRootElement(name = "sticky")
@XmlAccessorType(XmlAccessType.FIELD)
public class StickyLoadBalanceStrategy extends LoadBalancerType {
    @XmlElement(required = true, name = "expression", type = ExpressionType.class)
    private ExpressionType expressionType;
    @XmlElement(required = false, name = "loadBalancer", type = LoadBalancerType.class)
    private LoadBalancerType loadBalancerType;

    public StickyLoadBalanceStrategy() {
        super("org.apache.camel.processor.loadbalancer.StickyLoadBalancer");
    }

    public StickyLoadBalanceStrategy(ExpressionType expressionType) {
        this();
        this.expressionType = expressionType;
    }

    public StickyLoadBalanceStrategy(ExpressionType expressionType, LoadBalancerType loadBalancerType) {
        this();
        this.expressionType = expressionType;
        this.loadBalancerType = loadBalancerType;
    }

    public void setExpressionType(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setLoadBalancerType(LoadBalancerType loadBalancerType) {
        this.loadBalancerType = loadBalancerType;
    }

    public LoadBalancerType getLoadBalancerType() {
        return loadBalancerType;
    }

    @Override
    protected void configureLoadBalancer(LoadBalancer loadBalancer) {
        ExpressionType expression = getExpressionType();
        if (expression != null) {
            setProperty(loadBalancer, "correlationExpression", expression);
        }
        LoadBalancerType type = getLoadBalancerType();
        if (type != null) {
            setProperty(loadBalancer, "loadBalancer", type);
        }
    }

    @Override
    public String toString() {
        return "StickyLoadBalanceStrategy[" + expressionType + ", " + loadBalancerType + "]";
    }
}
