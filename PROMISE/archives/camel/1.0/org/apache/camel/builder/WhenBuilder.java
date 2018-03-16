package org.apache.camel.builder;

import org.apache.camel.Endpoint;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;

/**
 * @version $Revision: 550575 $
 */
public class WhenBuilder extends FilterBuilder {
    private final ChoiceBuilder parent;

    public WhenBuilder(ChoiceBuilder parent, Predicate predicate) {
        super(parent, predicate);
        this.parent = parent;
    }

    @Override
    @Fluent
    public ChoiceBuilder to(@FluentArg("ref")Endpoint endpoint) {
        super.to(endpoint);
        return parent;
    }

    @Override
    @Fluent
    public ChoiceBuilder to(@FluentArg("uri")String uri) {
        super.to(uri);
        return parent;
    }

    @Override
    @Fluent
    public ChoiceBuilder process(@FluentArg("ref")Processor processor) {
        super.process(processor);   
        return parent;
    }
}
