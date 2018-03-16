package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.Delayer;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;delayer/&gt; element
 *
 * @version $Revision: 719456 $
 */
@XmlRootElement(name = "delayer")
@XmlAccessorType(XmlAccessType.FIELD)
public class DelayerType extends ExpressionNode {
    @XmlElement
    private Long delay = 0L;

    public DelayerType() {
    }

    public DelayerType(Expression processAtExpression) {
        super(processAtExpression);
    }

    public DelayerType(ExpressionType processAtExpression) {
        super(processAtExpression);
    }

    public DelayerType(Expression processAtExpression, long delay) {
        super(processAtExpression);
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Delayer[" + getExpression() + " -> " + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "delayer";
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        Expression processAtExpression = createAbsoluteTimeDelayExpression(routeContext);
        return new Delayer(childProcessor, processAtExpression, delay);
    }

    private Expression createAbsoluteTimeDelayExpression(RouteContext routeContext) {
        ExpressionType expr = getExpression();
        if (expr != null) {
            if (ObjectHelper.isNotNullAndNonEmpty(expr.getExpression()) 
                || expr.getExpressionValue() != null) {
                return expr.createExpression(routeContext);
            } 
        } 
        return null;
    }
}
