package org.apache.camel.language.juel;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import de.odysseus.el.util.SimpleContext;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.ExpressionSupport;

/**
 * 
 * @version $Revision: $
 */
public class JuelExpression extends ExpressionSupport<Exchange> {

    private final String expression;
    private final Class<?> type;
    private ExpressionFactory expressionFactory;

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
            expressionFactory = ExpressionFactory.newInstance();
        }
        return expressionFactory;
    }

    public void setExpressionFactory(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    protected ELContext populateContext(ELContext context, Exchange exchange) {
        setVariable(context, "exchange", exchange, Exchange.class);
        setVariable(context, "in", exchange.getIn(), Message.class);
        setVariable(context, "out", exchange.getOut(), Message.class);
        return context;
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
        return new SimpleContext();
    }

    protected String assertionFailureMessage(Exchange exchange) {
        return expression;
    }
}
