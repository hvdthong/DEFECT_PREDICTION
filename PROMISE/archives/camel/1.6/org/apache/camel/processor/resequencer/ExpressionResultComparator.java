package org.apache.camel.processor.resequencer;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * A {@link SequenceElementComparator} that compares {@link Exchange}s based on
 * the result of an expression evaluation.
 * 
 * @author Martin Krasser
 * 
 * @version $Revision: 630591 $
 */
public interface ExpressionResultComparator<E extends Exchange> extends SequenceElementComparator<E> {

    /**
     * Sets the list expressions used for comparing {@link Exchange}s.
     * 
     * @param expressions a list of {@link Expression} objects.
     */
    void setExpressions(List<Expression> expressions);

}
