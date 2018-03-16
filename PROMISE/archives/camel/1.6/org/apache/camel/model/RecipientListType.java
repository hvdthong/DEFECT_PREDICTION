package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.processor.RecipientList;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;recipientList/&gt; element
 *
 * @version $Revision: 734353 $
 */
@XmlRootElement(name = "recipientList")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipientListType extends ExpressionNode {
    @XmlAttribute(required = false)
    private String delimiter;

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
        return "RecipientList[" + getExpression() + "]";
    }

    @Override
    public String getShortName() {
        return "recipientList";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Expression expression = getExpression().createExpression(routeContext);

        if (delimiter != null) {
            expression = ExpressionBuilder.tokenizeExpression(expression, delimiter);
        }

        return new RecipientList(expression);
    }


    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
