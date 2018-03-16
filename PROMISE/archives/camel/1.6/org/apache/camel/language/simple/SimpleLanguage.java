package org.apache.camel.language.simple;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.language.IllegalSyntaxException;
import org.apache.camel.util.ObjectHelper;

/**
 * which maps simple property style notations to access headers and bodies.
 * Examples of supported expressions are:
 * <ul>
 * <li>id to access the inbound message Id</li>
 * <li>in.body or body to access the inbound body</li>
 * <li>out.body to access the inbound body</li>
 * <li>in.header.foo or header.foo to access an inbound header called 'foo'</li>
 * <li>out.header.foo to access an outbound header called 'foo'</li>
 * <li>property.foo to access the exchange property called 'foo'</li>
 * <li>sys.foo to access the system property called 'foo'</li>
 * <li>date:&lt;command&gt;:&lt;pattern&gt; for date formatting using the {@link java.text.SimpleDateFormat} patterns.
 *     Supported commands are: <tt>now</tt> for current timestamp,
 *     <tt>in.header.xxx</tt> or <tt>header.xxx</tt> to use the Date object in the in header.
 *     <tt>out.header.xxx</tt> to use the Date object in the out header.
 * </li>
 * <li>bean:&lt;bean expression&gt; to invoke a bean using the
 * {@link org.apache.camel.language.bean.BeanLanguage BeanLanguage}</li>
 * </ul>
 *
 * @version $Revision: 698848 $
 */
public class SimpleLanguage extends AbstractSimpleLanguage {

    public static Expression simple(String expression) {
        SimpleLanguage language = new SimpleLanguage();
        return language.createExpression(expression);
    }

    protected Expression<Exchange> createSimpleExpression(String expression) {
        if (ObjectHelper.isEqualToAny(expression, "body", "in.body")) {
            return ExpressionBuilder.bodyExpression();
        } else if (ObjectHelper.equal(expression, "out.body")) {
            return ExpressionBuilder.outBodyExpression();
        } else if (ObjectHelper.equal(expression, "id")) {
            return ExpressionBuilder.messageIdExpression();
        }

        String remainder = ifStartsWithReturnRemainder("in.header.", expression);
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("header.", expression);
        }
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("headers.", expression);
        }
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("in.headers.", expression);
        }
        if (remainder != null) {
            return ExpressionBuilder.headerExpression(remainder);
        }

        remainder = ifStartsWithReturnRemainder("out.header.", expression);
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("out.headers.", expression);
        }
        if (remainder != null) {
            return ExpressionBuilder.outHeaderExpression(remainder);
        }

        remainder = ifStartsWithReturnRemainder("property.", expression);
        if (remainder != null) {
            return ExpressionBuilder.propertyExpression(remainder);
        }

        remainder = ifStartsWithReturnRemainder("sys.", expression);
        if (remainder != null) {
            return ExpressionBuilder.systemProperty(remainder);
        }

        remainder = ifStartsWithReturnRemainder("date:", expression);
        if (remainder != null) {
            String[] parts = remainder.split(":");
            if (parts.length != 2) {
                throw new IllegalSyntaxException(this, expression + " ${date:command:pattern} is the correct syntax.");
            }
            String command = parts[0];
            String pattern = parts[1];
            return ExpressionBuilder.dateExpression(command, pattern);
        }

        remainder = ifStartsWithReturnRemainder("bean:", expression);
        if (remainder != null) {
            return ExpressionBuilder.beanExpression(remainder);
        }

        throw new IllegalSyntaxException(this, expression);
    }

}
