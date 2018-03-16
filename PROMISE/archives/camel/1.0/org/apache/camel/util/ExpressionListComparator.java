package org.apache.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

import java.util.Comparator;
import java.util.List;

/**
 * An implementation of {@link java.util.Comparator} which takes a list of
 * {@link org.apache.camel.Expression} objects which is evaluated
 * on each exchange to compare them
 *
 * @version $Revision: 1.1 $
 */
public class ExpressionListComparator<E extends Exchange> implements Comparator<E> {
    private final List<Expression<E>> expressions;

    public ExpressionListComparator(List<Expression<E>> expressions) {
        this.expressions = expressions;
    }

    public int compare(E e1, E e2) {
        for (Expression<E> expression : expressions) {
            Object o1 = expression.evaluate(e1);
            Object o2 = expression.evaluate(e2);
            int answer = ObjectHelper.compare(o1, o2);
            if (answer != 0) {
                return answer;
            }
        }
        return 0;
    }
}
