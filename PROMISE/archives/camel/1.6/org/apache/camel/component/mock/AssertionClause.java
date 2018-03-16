package org.apache.camel.component.mock;


import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.ExpressionClause;
import org.apache.camel.builder.ValueBuilder;
import static org.apache.camel.builder.ExpressionBuilder.bodyExpression;
import static org.apache.camel.builder.ExpressionBuilder.headerExpression;
import static org.apache.camel.builder.ExpressionBuilder.propertyExpression;

/**
 * A builder of assertions on message exchanges
 *
 * @version $Revision: 702966 $
 */
public abstract class AssertionClause implements Runnable {

    private List<Predicate<Exchange>> predicates = new ArrayList<Predicate<Exchange>>();


    /**
     * Adds the given predicate to this assertion clause
     */
    public AssertionClause predicate(Predicate<Exchange> predicate) {
        addPredicate(predicate);
        return this;
    }

    public ExpressionClause<AssertionClause> predicate() {
        ExpressionClause<AssertionClause> clause = new ExpressionClause<AssertionClause>(this);
        addPredicate(clause);
        return clause;
    }

    /**
     * Returns a predicate and value builder for headers on an exchange
     */
    public ValueBuilder<Exchange> header(String name) {
        Expression<Exchange> expression = headerExpression(name);
        return new PredicateValueBuilder(expression);
    }

    /**
     * Returns a predicate and value builder for property on an exchange
     */
    public ValueBuilder<Exchange> property(String name) {
        Expression<Exchange> expression = propertyExpression(name);
        return new PredicateValueBuilder(expression);
    }

    /**
     * Returns a predicate and value builder for the inbound body on an exchange
     */
    public PredicateValueBuilder body() {
        Expression<Exchange> expression = bodyExpression();
        return new PredicateValueBuilder(expression);
    }

    /**
     * Returns a predicate and value builder for the inbound message body as a
     * specific type
     */
    public <T> PredicateValueBuilder bodyAs(Class<T> type) {
        Expression<Exchange> expression = bodyExpression(type);
        return new PredicateValueBuilder(expression);
    }

    /**
     * Returns a predicate and value builder for the outbound body on an
     * exchange
     */
    public PredicateValueBuilder outBody() {
        Expression<Exchange> expression = bodyExpression();
        return new PredicateValueBuilder(expression);
    }

    /**
     * Returns a predicate and value builder for the outbound message body as a
     * specific type
     */
    public <T> PredicateValueBuilder outBody(Class<T> type) {
        Expression<Exchange> expression = bodyExpression(type);
        return new PredicateValueBuilder(expression);
    }

    /**
     * Performs any assertions on the given exchange
     */
    protected void applyAssertionOn(MockEndpoint endpoint, int index, Exchange exchange) {
        for (Predicate<Exchange> predicate : predicates) {
            predicate.assertMatches(endpoint.getEndpointUri() + " ", exchange);
        }
    }

    protected void addPredicate(Predicate<Exchange> predicate) {
        predicates.add(predicate);
    }


    /**
     * Public class needed for fluent builders
     */
    public class PredicateValueBuilder extends ValueBuilder<Exchange> {

        public PredicateValueBuilder(Expression<Exchange> expression) {
            super(expression);
        }

        protected Predicate<Exchange> onNewPredicate(Predicate<Exchange> predicate) {
            addPredicate(predicate);
            return predicate;
        }
    }
}
