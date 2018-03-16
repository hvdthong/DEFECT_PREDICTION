package org.apache.camel.builder.sql;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.RuntimeExpressionException;
import org.apache.camel.util.ObjectHelper;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;

/**
 * A builder of SQL {@link org.apache.camel.Expression} and
 * {@link org.apache.camel.Predicate} implementations
 * 
 * @version $Revision: 630591 $
 */
public class SqlBuilder<E extends Exchange> implements Expression<E>, Predicate<E> {

    private Query query;
    private Map<String, Object> variables = new HashMap<String, Object>();

    public SqlBuilder(Query query) {
        this.query = query;
    }

    public Object evaluate(E exchange) {
        return evaluateQuery(exchange);
    }

    public boolean matches(E exchange) {
        List list = evaluateQuery(exchange);
        return matches(exchange, list);
    }

    public void assertMatches(String text, E exchange) throws AssertionError {
        List list = evaluateQuery(exchange);
        if (!matches(exchange, list)) {
            throw new AssertionError(this + " failed on " + exchange + " as found " + list);
        }
    }


    /**
     * Creates a new builder for the given SQL query string
     * 
     * @param sql the SQL query to perform
     * @return a new builder
     * @throws QueryParseException if there is an issue with the SQL
     */
    public static <E extends Exchange> SqlBuilder<E> sql(String sql) throws QueryParseException {
        Query q = new Query();
        q.parse(sql);
        return new SqlBuilder(q);
    }

    /**
     * Adds the variable value to be used by the SQL query
     */
    public SqlBuilder<E> variable(String name, Object value) {
        getVariables().put(name, value);
        return this;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> properties) {
        this.variables = properties;
    }

    protected boolean matches(E exchange, List list) {
        return ObjectHelper.matches(list);
    }

    protected List evaluateQuery(E exchange) {
        configureQuery(exchange);
        Message in = exchange.getIn();
        List list = in.getBody(List.class);
        if (list == null) {
            list = Collections.singletonList(in.getBody());
        }
        try {
            return query.execute(list).getResults();
        } catch (QueryExecutionException e) {
            throw new RuntimeExpressionException(e);
        }
    }

    protected void configureQuery(E exchange) {
        addVariables(exchange.getProperties());
        addVariables(exchange.getIn().getHeaders());
        addVariables(getVariables());

        query.setVariable("exchange", exchange);
        query.setVariable("in", exchange.getIn());
        query.setVariable("out", exchange.getOut());
    }

    protected void addVariables(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> propertyEntries = map.entrySet();
        for (Map.Entry<String, Object> entry : propertyEntries) {
            query.setVariable(entry.getKey(), entry.getValue());
        }
    }
}
