package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.ProcessorBuilder;
import org.apache.camel.model.language.ExpressionType;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;setOutHeader/&gt; element
 */
@XmlRootElement(name = "setOutHeader")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetOutHeaderType extends ExpressionNode {
    @XmlAttribute(required = true)
    private String headerName;
    
    public SetOutHeaderType() {
    }

    public SetOutHeaderType(String headerName, ExpressionType expression) {
        super(expression);
        setHeaderName(headerName);
    }

    public SetOutHeaderType(String headerName, Expression expression) {
        super(expression);
        setHeaderName(headerName);        
    }

    @Override
    public String toString() {
        return "SetOutHeader[" + getHeaderName() + ", " + getExpression() + "]";
    }

    @Override
    public String getShortName() {
        return "setOutHeader";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        ObjectHelper.notNull(getHeaderName(), "headerName");
        Expression expr = getExpression().createExpression(routeContext);
        return ProcessorBuilder.setOutHeader(getHeaderName(), expr);
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }
}
