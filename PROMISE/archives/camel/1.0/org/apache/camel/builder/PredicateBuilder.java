package org.apache.camel.builder;

import static org.apache.camel.util.ObjectHelper.compare;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.impl.PredicateSupport;
import org.apache.camel.impl.BinaryPredicateSupport;
import org.apache.camel.util.ObjectHelper;
import static org.apache.camel.util.ObjectHelper.notNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class for working with predicates
 *
 * @version $Revision: 520261 $
 */
public class PredicateBuilder {
    /**
     * A helper method to combine multiple predicates by a logical AND
     */
    public static <E extends Exchange> Predicate<E> and(final Predicate<E> left, final Predicate<E> right) {
        notNull(left, "left");
        notNull(right, "right");
        return new PredicateSupport<E>() {
            public boolean matches(E exchange) {
                return left.matches(exchange) && right.matches(exchange);
            }

            @Override
            public String toString() {
                return "(" + left + ") and (" + right + ")";
            }
        };
    }

    /**
     * A helper method to combine multiple predicates by a logical OR
     */
    public static <E extends Exchange> Predicate<E> or(final Predicate<E> left, final Predicate<E> right) {
        notNull(left, "left");
        notNull(right, "right");
        return new PredicateSupport<E>() {
            public boolean matches(E exchange) {
                return left.matches(exchange) || right.matches(exchange);
            }

            @Override
            public String toString() {
                return "(" + left + ") or (" + right + ")";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isEqualTo(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return ObjectHelper.equals(leftValue, rightValue);
            }

            protected String getOperationText() {
                return "==";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isNotEqualTo(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return !ObjectHelper.equals(leftValue, rightValue);
            }

            protected String getOperationText() {
                return "==";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isLessThan(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return compare(leftValue, rightValue) < 0;
            }

            protected String getOperationText() {
                return "<";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isLessThanOrEqualTo(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return compare(leftValue, rightValue) <= 0;
            }

            protected String getOperationText() {
                return "<=";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isGreaterThan(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return compare(leftValue, rightValue) > 0;
            }

            protected String getOperationText() {
                return ">";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isGreaterThanOrEqualTo(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return compare(leftValue, rightValue) < 0;
            }

            protected String getOperationText() {
                return ">=";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> contains(final Expression<E> left, final Expression<E> right) {
        return new BinaryPredicateSupport<E>(left, right) {

            protected boolean matches(E exchange, Object leftValue, Object rightValue) {
                return ObjectHelper.contains(leftValue, rightValue);
            }

            protected String getOperationText() {
                return "contains";
            }
        };
    }

    public static <E extends Exchange> Predicate<E> isNull(final Expression<E> expression) {
        return isEqualTo(expression, ExpressionBuilder.<E>constantExpression(null));
    }

    public static <E extends Exchange> Predicate<E> isNotNull(final Expression<E> expression) {
        return isNotEqualTo(expression, ExpressionBuilder.<E>constantExpression(null));
    }

    public static <E extends Exchange> Predicate<E> isInstanceOf(final Expression<E> expression, final Class type) {
        notNull(expression, "expression");
        notNull(type, "type");

        return new PredicateSupport<E>() {
            public boolean matches(E exchange) {
                Object value = expression.evaluate(exchange);
                return type.isInstance(value);
            }

            @Override
            public String toString() {
                return expression + " instanceof " + type.getName();
            }

            @Override
            protected String assertionFailureMessage(E exchange) {
                return super.assertionFailureMessage(exchange) + " for <" + expression.evaluate(exchange) + ">";
            }
        };
    }


    /**
     * Returns a predicate which is true if the expression matches the given regular expression
     *
     * @param expression the expression to evaluate
     * @param regex the regular expression to match against
     * @return a new predicate
     */
    public static <E extends Exchange> Predicate<E> regex(final Expression<E> expression, final String regex) {
        return regex(expression, Pattern.compile(regex));
    }

    /**
     * Returns a predicate which is true if the expression matches the given regular expression
     *
     * @param expression the expression to evaluate
     * @param pattern the regular expression to match against
     * @return a new predicate
     */
    public static <E extends Exchange> Predicate<E> regex(final Expression<E> expression, final Pattern pattern) {
        notNull(expression, "expression");
        notNull(pattern, "pattern");

        return new PredicateSupport<E>() {
            public boolean matches(E exchange) {
                Object value = expression.evaluate(exchange);
                if (value != null) {
                    Matcher matcher = pattern.matcher(value.toString());
                    return matcher.matches();
                }
                return false;
            }

            @Override
            public String toString() {
                return expression + ".matches(" + pattern + ")";
            }

            @Override
            protected String assertionFailureMessage(E exchange) {
                return super.assertionFailureMessage(exchange) + " for <" + expression.evaluate(exchange) + ">";
            }

        };
    }

}
