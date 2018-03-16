package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.language.bean.BeanExpression;
import org.apache.camel.spi.RouteContext;

/**
 * For expresions and predicates using the
 *
 * @version $Revision: 659197 $
 */
@XmlRootElement(name = "methodCall")
@XmlAccessorType(XmlAccessType.FIELD)
public class MethodCallExpression extends ExpressionType {
    @XmlAttribute(required = false)
    private String bean;
    @XmlAttribute(required = false)
    private String method;

    public MethodCallExpression() {
    }

    public MethodCallExpression(String beanName) {
        super(beanName);
    }

    public MethodCallExpression(String beanName, String method) {
        super(beanName);
        this.method = method;
    }

    public String getLanguage() {
        return "bean";
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public Expression createExpression(RouteContext routeContext) {
        return new BeanExpression(beanName(), getMethod());
    }

    @Override
    public Predicate<Exchange> createPredicate(RouteContext routeContext) {
        return new BeanExpression<Exchange>(beanName(), getMethod());
    }

    protected String beanName() {
        if (bean != null) {
            return bean;
        }
        return getExpression();
    }
}
