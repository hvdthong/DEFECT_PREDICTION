package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Predicate;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.FilterProcessor;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterType extends ExpressionNode {
    public FilterType() {
    }

    public FilterType(ExpressionType expression) {
        super(expression);
    }

    public FilterType(Predicate predicate) {
        super(predicate);
    }

    @Override
    public String toString() {
        return "Filter[ " + getExpression() + " -> " + getOutputs() + "]";
    }

    @Override
    public FilterProcessor createProcessor(RouteContext routeContext) throws Exception {
        return createFilterProcessor(routeContext);
    }
}
