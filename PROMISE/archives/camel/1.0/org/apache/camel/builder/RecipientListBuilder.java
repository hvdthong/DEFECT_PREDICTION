package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.processor.RecipientList;

/**
 *
 * @version $Revision: 532790 $
 */
public class RecipientListBuilder<E extends Exchange> extends BuilderSupport implements ProcessorFactory {
    private final Expression expression;


    public RecipientListBuilder(FromBuilder parent, Expression expression) {
        super(parent);
        this.expression = expression;
    }

    public Processor createProcessor() {
        return new RecipientList(expression);
    }
}
