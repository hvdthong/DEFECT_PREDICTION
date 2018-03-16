package org.apache.camel.component.jpa;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.converter.ObjectConverter;
import org.apache.camel.impl.DefaultProducer;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Iterator;

/**
 * @version $Revision: 525547 $
 */
public class JpaProducer extends DefaultProducer<Exchange> {
    private final TransactionStrategy template;
    private final Expression<Exchange> expression;

    public JpaProducer(JpaEndpoint endpoint, Expression<Exchange> expression) {
        super(endpoint);
        this.expression = expression;
        this.template = endpoint.createTransactionStrategy();
    }

    public void process(Exchange exchange) {
        final Object values = expression.evaluate(exchange);
        if (values != null) {
            template.execute(new JpaCallback() {
                public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                    Iterator iter = ObjectConverter.iterator(values);
                    while (iter.hasNext()) {
                        Object value = iter.next();
                        entityManager.persist(value);
                    }
                    return null;
                }
            });
        }
    }
}
