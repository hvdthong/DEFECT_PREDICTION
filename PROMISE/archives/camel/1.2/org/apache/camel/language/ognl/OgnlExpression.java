package org.apache.camel.language.ognl;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.impl.ExpressionSupport;
import org.apache.camel.language.ExpressionEvaluationException;
import org.apache.camel.language.IllegalSyntaxException;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @version $Revision: $
 */
public class OgnlExpression extends ExpressionSupport<Exchange> {

    private final String expressionString;
    private final Class<?> type;
    private Object expression;

    public OgnlExpression(OgnlLanguage language, String expressionString, Class<?> type) {
        this.expressionString = expressionString;
        this.type = type;
        try {
            this.expression = Ognl.parseExpression(expressionString);
        } catch (OgnlException e) {
            throw new IllegalSyntaxException(language, expressionString);
        }
    }

    public static OgnlExpression ognl(String expression) {
        return new OgnlExpression(new OgnlLanguage(), expression, Object.class);
    }

    public Object evaluate(Exchange exchange) {
        Map values = new HashMap();
        populateContext(values, exchange);
        OgnlContext oglContext = new OgnlContext();
        try {
            return Ognl.getValue(expression, oglContext, new RootObject(exchange));
        } catch (OgnlException e) {
            throw new ExpressionEvaluationException(this, exchange, e);
        }
    }

    protected void populateContext(Map map, Exchange exchange) {
        map.put("exchange", exchange);
        map.put("in", exchange.getIn());
        map.put("out", exchange.getOut());
    }

    protected String assertionFailureMessage(Exchange exchange) {
        return expressionString;
    }
}
