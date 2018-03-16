package org.apache.camel.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.FilterProcessor;
import org.apache.camel.spi.RouteContext;

/**
 * A base class for nodes which contain an expression and a number of outputs
 *
 * @version $Revision: 671918 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpressionNode extends ProcessorType<ProcessorType> {
    @XmlElementRef
    private ExpressionType expression;
    @XmlElementRef
    private List<ProcessorType<?>> outputs = new ArrayList<ProcessorType<?>>();

    public ExpressionNode() {
    }

    public ExpressionNode(ExpressionType expression) {
        this.expression = expression;
    }

    public ExpressionNode(Expression expression) {
        if (expression != null) {
            setExpression(new ExpressionType(expression));
        }
    }

    public ExpressionNode(Predicate predicate) {
        if (predicate != null) {
            setExpression(new ExpressionType(predicate));
        }
    }

    @Override
    public String getShortName() {
        return "exp";
    }
      
    public ExpressionType getExpression() {
        return expression;
    }

    public void setExpression(ExpressionType expression) {
        this.expression = expression;
    }

    public List<ProcessorType<?>> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ProcessorType<?>> outputs) {
        this.outputs = outputs;
    }

    @Override
    public String getLabel() {
        if (getExpression() == null) {
            return "";
        }
        return getExpression().getLabel();
    }

    protected FilterProcessor createFilterProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        return new FilterProcessor(getExpression().createPredicate(routeContext), childProcessor);
    }

    @Override
    protected void configureChild(ProcessorType output) {
        super.configureChild(output);
        if (isInheritErrorHandler()) {
            output.setErrorHandlerBuilder(getErrorHandlerBuilder());
        }
    }
}
