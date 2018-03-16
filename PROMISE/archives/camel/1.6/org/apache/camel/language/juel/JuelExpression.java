package org.apache.camel.language.juel;

import java.util.Properties;

import javax.el.ArrayELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;
import de.odysseus.el.util.SimpleContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.ExpressionSupport;



/**
 *
 * @version $Revision: 640731 $
 */
public class JuelExpression extends ExpressionSupport<Exchange> {
    private final String expression;
    private final Class<?> type;
    private ExpressionFactory expressionFactory;
    private Properties expressionFactoryProperties;

    public JuelExpression(String expression, Class<?> type) {
        this.expression = expression;
        this.type = type;
    }

    public static JuelExpression el(String expression) {
        return new JuelExpression(expression, Object.class);
    }

    public Object evaluate(Exchange exchange) {
        ELContext context = populateContext(createContext(), exchange);
        ValueExpression valueExpression = getExpressionFactory().createValueExpression(context, expression, type);
        return valueExpression.getValue(context);
    }

    public ExpressionFactory getExpressionFactory() {
        if (expressionFactory == null) {
            Properties properties = getExpressionFactoryProperties();
            expressionFactory = ExpressionFactory.newInstance(properties);
        }
        return expressionFactory;
    }

    public void setExpressionFactory(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    public Properties getExpressionFactoryProperties() {
        if (expressionFactoryProperties == null) {
            expressionFactoryProperties = new Properties();
            populateDefaultExpressionProperties(expressionFactoryProperties);
        }
        return expressionFactoryProperties;
    }

    public void setExpressionFactoryProperties(Properties expressionFactoryProperties) {
        this.expressionFactoryProperties = expressionFactoryProperties;
    }

    protected ELContext populateContext(ELContext context, Exchange exchange) {
        setVariable(context, "exchange", exchange, Exchange.class);
        setVariable(context, "in", exchange.getIn(), Message.class);
        Message out = exchange.getOut(false);
        setVariable(context, "out", out, Message.class);
        return context;
    }

    /**
     * A Strategy Method to populate the default properties used to create the expression factory
     */
    protected void populateDefaultExpressionProperties(Properties properties) {
        properties.setProperty("javax.el.methodInvocations", "true");
    }

    protected void setVariable(ELContext context, String name, Object value, Class<?> type) {
        ValueExpression valueExpression = getExpressionFactory().createValueExpression(value, type);
        SimpleContext simpleContext = (SimpleContext) context;
        simpleContext.setVariable(name, valueExpression);
    }

    /**
     * Factory method to create the EL context
     */
    protected ELContext createContext() {
        ELResolver resolver = new CompositeELResolver() {
            {
                add(new ArrayELResolver(false));
                add(new ListELResolver(false));
                add(new MapELResolver(false));
                add(new ResourceBundleELResolver());
                add(new BeanAndMethodELResolver());
            }
        };
        return new SimpleContext(resolver);
    }

    protected String assertionFailureMessage(Exchange exchange) {
        return expression;
    }
}
