package org.apache.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

import java.util.Comparator;

/**
 * An implementation of {@link Comparator} which takes an {@link Expression} which is evaluated
 * on each exchange to compare
 *  
 * @version $Revision: 1.1 $
 */
public class ExpressionComparator<E extends Exchange> implements Comparator<E> {
    private final Expression<E> expression;

    public ExpressionComparator(Expression<E> expression) {
        this.expression = expression;
    }

    public int compare(E e1, E e2) {
        Object o1 = expression.evaluate(e1);
        Object o2 = expression.evaluate(e2);
        return ObjectHelper.compare(o1, o2);
    }
}
