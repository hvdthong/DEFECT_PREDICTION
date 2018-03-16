package org.apache.camel.component.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import org.apache.camel.impl.ServiceSupport;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Delegates the strategy to the {@link JpaTemplate} and {@link TransactionTemplate} for transaction handling
 *
 * @version $Revision: 630591 $
 */
public class JpaTemplateTransactionStrategy extends ServiceSupport implements TransactionStrategy {
    private final JpaTemplate jpaTemplate;
    private final TransactionTemplate transactionTemplate;

    public JpaTemplateTransactionStrategy(JpaTemplate jpaTemplate, TransactionTemplate transactionTemplate) {
        this.jpaTemplate = jpaTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * Creates a new implementation from the given JPA factory
     */
    public static JpaTemplateTransactionStrategy newInstance(EntityManagerFactory emf) {
        JpaTemplate template = new JpaTemplate(emf);
        return newInstance(emf, template);
    }

    public static JpaTemplateTransactionStrategy newInstance(EntityManagerFactory emf, JpaTemplate template) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
        transactionManager.afterPropertiesSet();

        TransactionTemplate tranasctionTemplate = new TransactionTemplate(transactionManager);
        tranasctionTemplate.afterPropertiesSet();

        return new JpaTemplateTransactionStrategy(template, tranasctionTemplate);
    }

    public Object execute(final JpaCallback callback) {
        return transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                return jpaTemplate.execute(new JpaCallback() {
                    public Object doInJpa(EntityManager entityManager) throws PersistenceException {
                        return callback.doInJpa(entityManager);
                    }
                });
            }
        });
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }
}
