package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.Delayer;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;delayer/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "delayer")
@XmlAccessorType(XmlAccessType.FIELD)
public class DelayerType extends ExpressionNode {
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
        return "Delayer[ " + getExpression() + " -> " + getOutputs() + "]";
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
        Expression processAtExpression = getExpression() != null ? getExpression().createExpression(routeContext) : null;
        return new Delayer(childProcessor, processAtExpression, delay);
    }
}
