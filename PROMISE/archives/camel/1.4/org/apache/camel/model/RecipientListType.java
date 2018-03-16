package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.RecipientList;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;recipientList/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "recipientList")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipientListType extends ExpressionNode {
    public RecipientListType() {
    }

    public RecipientListType(ExpressionType expression) {
        super(expression);
    }

    public RecipientListType(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "RecipientList[ " + getExpression() + "]";
    }

    @Override
    public String getShortName() {
        return "recipientList";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return new RecipientList(getExpression().createExpression(routeContext));
    }
}
