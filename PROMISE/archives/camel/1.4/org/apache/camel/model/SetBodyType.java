package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.ProcessorBuilder;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;setBody/&gt; element.
 */
@XmlRootElement(name = "setBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetBodyType extends ExpressionNode {

    public SetBodyType() {
    }

    public SetBodyType(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "SetBody[ " + getExpression() + "]";
    }

    @Override
    public String getShortName() {
        return "setBody";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Expression expr = getExpression().createExpression(routeContext);
        return ProcessorBuilder.setBody(expr);
    }
}
