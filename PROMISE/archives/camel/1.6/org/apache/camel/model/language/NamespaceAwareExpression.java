package org.apache.camel.model.language;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.NamespaceAware;
import org.apache.camel.spi.RouteContext;

/**
 * A useful base class for any expression which may be namespace or XML content aware
 * such as {@link XPathExpression} or {@link XQueryExpression}
 *
 * @version $Revision: 659007 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class NamespaceAwareExpression extends ExpressionType implements NamespaceAware {
    @XmlTransient
    private Map<String, String> namespaces;

    public NamespaceAwareExpression() {
    }

    public NamespaceAwareExpression(String expression) {
        super(expression);
    }


    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    /**
     * Injects the XML Namespaces of prefix -> uri mappings
     *
     * @param namespaces the XML namespaces with the key of prefixes and the value the URIs
     */
    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    @Override
    protected void configureExpression(RouteContext routeContext, Expression expression) {
        configureNamespaceAware(expression);
    }

    @Override
    protected void configurePredicate(RouteContext routeContext, Predicate predicate) {
        configureNamespaceAware(predicate);
    }

    protected void configureNamespaceAware(Object builder) {
        if (namespaces != null && builder instanceof NamespaceAware) {
            NamespaceAware namespaceAware = (NamespaceAware) builder;
            namespaceAware.setNamespaces(namespaces);
        }
    }
}
