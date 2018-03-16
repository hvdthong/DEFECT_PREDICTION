package org.apache.camel.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.processor.ChoiceProcessor;
import org.apache.camel.processor.FilterProcessor;

/**
 * @version $Revision: 532790 $
 */
public class ChoiceBuilder extends FromBuilder {

    private final FromBuilder parent;
    private List<WhenBuilder> predicateBuilders = new ArrayList<WhenBuilder>();
    private FromBuilder otherwise;

    public ChoiceBuilder(FromBuilder parent) {
        super(parent);
        this.parent = parent;
    }

    /**
     * Adds a predicate which if it is true then the message exchange is sent to the given destination
     *
     * @return a builder for creating a when predicate clause and action
     */
    @Fluent(nestedActions=true)
    public WhenBuilder when(
    		@FluentArg(value="predicate",element=true) 
    		Predicate predicate) {
        WhenBuilder answer = new WhenBuilder(this, predicate);
        predicateBuilders.add(answer);
        return answer;
    }

    @Fluent(nestedActions=true)
    public FromBuilder otherwise() {
        this.otherwise = new FromBuilder(parent);
        return otherwise;
    }

    public List<WhenBuilder> getPredicateBuilders() {
        return predicateBuilders;
    }

    public FromBuilder getOtherwise() {
        return otherwise;
    }

    @Override
    public Processor createProcessor() throws Exception {
        List<FilterProcessor> filters = new ArrayList<FilterProcessor>();
        for (WhenBuilder predicateBuilder : predicateBuilders) {
            filters.add(predicateBuilder.createProcessor());
        }
        Processor otherwiseProcessor = null;
        if (otherwise != null) {
            otherwiseProcessor = otherwise.createProcessor();
        }
        return new ChoiceProcessor(filters, otherwiseProcessor);
    }
}
