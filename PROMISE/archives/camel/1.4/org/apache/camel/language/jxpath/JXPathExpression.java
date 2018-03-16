package org.apache.camel.language.jxpath;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.impl.ExpressionSupport;
import org.apache.camel.language.ExpressionEvaluationException;
import org.apache.commons.jxpath.CompiledExpression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;

/**
 */
public class JXPathExpression extends ExpressionSupport<Exchange> {

    private String expression;
    private CompiledExpression compiledExpression;
    private final Class<?> type;

    /**
     * Creates a new JXPathExpression instance
     * 
     * @param expression the JXPath expression to be evaluated
     * @param type the expected result type
     */
    public JXPathExpression(String expression, Class<?> type) {
        super();
        this.expression = expression;
        this.type = type;
    }


    public Object evaluate(Exchange exchange) {
        try {
            JXPathContext context = JXPathContext.newContext(exchange);
            Object result = getJXPathExpression().getValue(context, type);
            assertResultType(exchange, result);
            return result;
        } catch (JXPathException e) {
            throw new ExpressionEvaluationException(this, exchange, e);
        }
    }

    /*
     * Check if the result is of the specified type
     */
    private void assertResultType(Exchange exchange, Object result) {
        if (result != null && !type.isAssignableFrom(result.getClass())) {
            throw new JXPathException("JXPath result type is " + result.getClass() + " instead of required type " + type);
        }
    }

    @Override
    protected String assertionFailureMessage(Exchange exchange) {
        return expression.toString();
    }

    /*
     * Get a compiled expression instance for better performance
     */
    private synchronized CompiledExpression getJXPathExpression() {
        if (compiledExpression == null) {
            compiledExpression = JXPathContext.compile(expression);
        }
        return compiledExpression;
    }
}
