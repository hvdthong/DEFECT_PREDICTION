package org.apache.camel.model.language;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.RouteContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * For XQuery expressions and predicates
 *
 * @version $Revision: 679483 $
 */
@XmlRootElement(name = "xquery")
@XmlAccessorType(XmlAccessType.FIELD)
public class XQueryExpression extends NamespaceAwareExpression {
    private static final transient Log LOG = LogFactory.getLog(XQueryExpression.class);

    @XmlAttribute(required = false)
    private String type;
    @XmlTransient
    private Class resultType;

    public XQueryExpression() {
    }

    public XQueryExpression(String expression) {
        super(expression);
    }

    public String getLanguage() {
        return "xquery";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        updateResultType();
        if (resultType != null) {
            setProperty(expression, "resultType", resultType);
        }
    }

    @Override
    protected void configurePredicate(RouteContext routeContext, Predicate predicate) {
        super.configurePredicate(routeContext, predicate);
        updateResultType();
        if (resultType != null) {
            setProperty(predicate, "resultType", resultType);
        }
    }

    private void updateResultType() {
        if (resultType == null && type != null) {
            try {
                resultType = Class.forName(type);
            } catch (ClassNotFoundException e) {
                LOG.error("ClassNotFoundException creating class: " + type);
            }
        }
    }
}
