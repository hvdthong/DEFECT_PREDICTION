package org.apache.camel.util;

import java.util.Comparator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * An implementation of {@link java.util.Comparator} which takes a list of
 * {@link org.apache.camel.Expression} objects which is evaluated
 * on each exchange to compare them
 *
 * @version $Revision: 1.1 $
 */
public class ExpressionListComparator implements Comparator<Exchange> {
    private final List<Expression> expressions;

    public ExpressionListComparator(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public int compare(Exchange e1, Exchange e2) {
        for (Expression expression : expressions) {
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
