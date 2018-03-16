package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.RouteContext;

/**
 * For XPath expresions and predicates
 *
 * @version $Revision: 659007 $
 */
@XmlRootElement(name = "xpath")
@XmlAccessorType(XmlAccessType.FIELD)
public class XPathExpression extends NamespaceAwareExpression {
    @XmlAttribute(required = false)
    private Class resultType;

    public XPathExpression() {
    }

    public XPathExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "xpath";
    }

    public Class getResultType() {
        return resultType;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }

    @Override
    protected void configureExpression(RouteContext routeContext, Expression expression) {
        super.configureExpression(routeContext, expression);
        if (resultType != null) {
            setProperty(expression, "resultType", resultType);
        }
    }

    @Override
    protected void configurePredicate(RouteContext routeContext, Predicate predicate) {
        super.configurePredicate(routeContext, predicate);
        if (resultType != null) {
            setProperty(predicate, "resultType", resultType);
        }
    }
}
