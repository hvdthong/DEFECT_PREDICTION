package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.LoopProcessor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;loop/&gt; element
 *
 * @version $Revision: 705880 $
 */
@XmlRootElement(name = "loop")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoopType extends ExpressionNode implements Block {
    public LoopType() {
    }

    public LoopType(Expression expression) {
        super(expression);
    }

    public LoopType(ExpressionType expression) {
        super(expression);
    }

    public void setExpression(Expression<?> expr) {
        if (expr != null) {
            setExpression(new ExpressionType(expr));
        }
    }
    @Override
    public String toString() {
        return "Loop[" + getExpression() + " -> " + getOutputs() + "]";
    }
    
    @Override
    public String getShortName() {
        return "loop";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return new LoopProcessor(
            getExpression().createExpression(routeContext),
            routeContext.createProcessor(this));
    }
}
