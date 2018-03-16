package org.apache.camel.builder;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.processor.Splitter;

/**
 * where an expression is evaluated to iterate through each of the parts of a message and then each part is then send to some endpoint.

 * @version $Revision: 534145 $
 */
public class SplitterBuilder extends FromBuilder {
    private final Expression expression;

    public SplitterBuilder(FromBuilder parent, Expression expression) {
        super(parent);
        this.expression = expression;
    }

    public Processor createProcessor() throws Exception {
        Processor destination = super.createProcessor();
        return new Splitter(destination, expression);
    }
}
