package org.apache.camel.processor.idempotent.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.camel.processor.idempotent.MessageIdRepository;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @version $Revision: 630591 $
 */
public class JpaMessageIdRepository implements MessageIdRepository {
    protected static final String QUERY_STRING = "select x from " + MessageProcessed.class.getName() + " x where x.processorName = ?1 and x.messageId = ?2";
    private JpaTemplate jpaTemplate;
    private String processorName;
    private TransactionTemplate transactionTemplate;

    public JpaMessageIdRepository(JpaTemplate template, String processorName) {
        this(template, createTransactionTemplate(template), processorName);
    }

    public JpaMessageIdRepository(JpaTemplate template, TransactionTemplate transactionTemplate, String processorName) {
        this.jpaTemplate = template;
        this.processorName = processorName;
        this.transactionTemplate = transactionTemplate;
    }

    public static JpaMessageIdRepository jpaMessageIdRepository(String persistenceUnit, String processorName) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
        return jpaMessageIdRepository(new JpaTemplate(entityManagerFactory), processorName);
    }

    public static JpaMessageIdRepository jpaMessageIdRepository(JpaTemplate jpaTemplate, String processorName) {
        return new JpaMessageIdRepository(jpaTemplate, processorName);
    }

    private static TransactionTemplate createTransactionTemplate(JpaTemplate jpaTemplate) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(new JpaTransactionManager(jpaTemplate.getEntityManagerFactory()));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate;
    }

    public boolean contains(final String messageId) {
        Boolean rc = (Boolean)transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus arg0) {

                List list = jpaTemplate.find(QUERY_STRING, processorName, messageId);
                if (list.isEmpty()) {
                    MessageProcessed processed = new MessageProcessed();
                    processed.setProcessorName(processorName);
                    processed.setMessageId(messageId);
                    jpaTemplate.persist(processed);
                    jpaTemplate.flush();
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            }
        });
        return rc.booleanValue();
    }
}
