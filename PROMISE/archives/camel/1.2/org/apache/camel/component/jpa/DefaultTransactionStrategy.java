package org.apache.camel.component.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.camel.impl.ServiceSupport;

import org.springframework.orm.jpa.JpaCallback;

import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * @version $Revision: 563665 $
 */
public class DefaultTransactionStrategy extends ServiceSupport implements TransactionStrategy {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public DefaultTransactionStrategy(EntityManagerFactory entityManagerFactory) {
        notNull(entityManagerFactory, "entityManagerFactory");
        this.entityManagerFactory = entityManagerFactory;
    }

    public DefaultTransactionStrategy(EntityManager entityManager) {
        notNull(entityManager, "entityManager");
        this.entityManager = entityManager;
    }

    public Object execute(JpaCallback callback) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            Object answer = callback.doInJpa(em);
            transaction.commit();
            return answer;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

    protected void doStart() throws Exception {
        getEntityManager();
    }

    protected void doStop() throws Exception {
        if (entityManager != null) {
            entityManager.close();
        }
    }
}
