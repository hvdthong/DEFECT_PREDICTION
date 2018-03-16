package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.Splitter;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "splitter")
@XmlAccessorType(XmlAccessType.FIELD)
public class SplitterType extends ExpressionNode {
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
        return "Splitter[ " + getExpression() + " -> " + getOutputs() + "]";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        return new Splitter(getExpression().createExpression(routeContext), childProcessor);
    }
}
