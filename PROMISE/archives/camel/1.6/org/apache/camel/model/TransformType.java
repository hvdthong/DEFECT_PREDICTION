package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.processor.TransformProcessor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;transform/&gt; element
 */
@XmlRootElement(name = "transform")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransformType extends ExpressionNode {

    public TransformType() {
    }

    public TransformType(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "Transform[" + getExpression() + "]";
    }

    @Override
    public String getShortName() {
        return "transform";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Expression expr = getExpression().createExpression(routeContext);
        Processor childProcessor = routeContext.createProcessor(this);

        return new TransformProcessor(expr, childProcessor);
    }
}
