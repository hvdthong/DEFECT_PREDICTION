package org.apache.camel.processor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.util.ExpressionComparator;
import org.apache.camel.util.ExpressionListComparator;

/**
 * which can reorder messages within a batch.
 *
 * @version $Revision: 724629 $
 */
public class Resequencer extends BatchProcessor {
    public Resequencer(Processor processor, Expression expression) {
        this(processor, createSet(expression));
    }

    public Resequencer(Processor processor, List<Expression> expressions) {
        this(processor, createSet(expressions));
    }

    public Resequencer(Processor processor, Set<Exchange> collection) {
        super(processor, collection);
    }

    @Override
    public String toString() {
        return "Resequencer[to: " + getProcessor() + "]";
    }


    protected static Set<Exchange> createSet(Expression<Exchange> expression) {
        return createSet(new ExpressionComparator<Exchange>(expression));
    }

    protected static Set<Exchange> createSet(List<Expression> expressions) {
        if (expressions.size() == 1) {
            return createSet(expressions.get(0));
        }
        return createSet(new ExpressionListComparator(expressions));
    }

    protected static Set<Exchange> createSet(Comparator<? super Exchange> comparator) {
        return new TreeSet<Exchange>(comparator);
    }
}
