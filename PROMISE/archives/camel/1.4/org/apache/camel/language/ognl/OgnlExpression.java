package org.apache.camel.language.ognl;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.impl.ExpressionSupport;
import org.apache.camel.language.ExpressionEvaluationException;
import org.apache.camel.language.IllegalSyntaxException;

/**
 *
 * @version $Revision: 641674 $
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
        OgnlContext oglContext = new OgnlContext();
        try {
            return Ognl.getValue(expression, oglContext, new RootObject(exchange));
        } catch (OgnlException e) {
            throw new ExpressionEvaluationException(this, exchange, e);
        }
    }

    protected String assertionFailureMessage(Exchange exchange) {
        return expressionString;
    }
}
