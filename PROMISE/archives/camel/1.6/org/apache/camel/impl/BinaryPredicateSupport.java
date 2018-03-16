package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * A useful base class for {@link Predicate} implementations
 * 
 * @deprecated is a duplicate of the same class in the builder package. Will be removed in Camel 2.0
 * @version $Revision: 729898 $
 */
public abstract class BinaryPredicateSupport<E extends Exchange> implements Predicate<E> {

    private final Expression<E> left;
    private final Expression<E> right;

    protected BinaryPredicateSupport(Expression<E> left, Expression<E> right) {
        notNull(left, "left");
        notNull(right, "right");

        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " " + getOperationText() + " " + right;
    }

    public boolean matches(E exchange) {
        Object leftValue = left.evaluate(exchange);
        Object rightValue = right.evaluate(exchange);
        return matches(exchange, leftValue, rightValue);
    }

    public void assertMatches(String text, E exchange) {
        Object leftValue = left.evaluate(exchange);
        Object rightValue = right.evaluate(exchange);
        if (!matches(exchange, leftValue, rightValue)) {
            throw new AssertionError(text + assertionFailureMessage(exchange, leftValue, rightValue));
        }
    }

    protected abstract boolean matches(E exchange, Object leftValue, Object rightValue);

    protected abstract String getOperationText();

    protected String assertionFailureMessage(E exchange, Object leftValue, Object rightValue) {
        return this + " failed on " + exchange + " with left value <" + leftValue + "> right value <"
               + rightValue + ">";
    }
}
