package org.apache.camel.spring.spi;

import org.apache.camel.Processor;
import org.apache.camel.spi.Policy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wraps the processor in a Spring transaction
 *
 * @version $Revision: 1.1 $
 */
public class SpringTransactionPolicy<E> implements Policy<E> {
    private static final transient Log LOG = LogFactory.getLog(SpringTransactionPolicy.class);
    private TransactionTemplate template;

    public SpringTransactionPolicy(TransactionTemplate template) {
        this.template = template;
    }

    public Processor wrap(Processor processor) {
        final TransactionTemplate transactionTemplate = getTemplate();
        if (transactionTemplate == null) {
            LOG.warn("No TransactionTemplate available so transactions will not be enabled!");
            return processor;
        }

        TransactionInterceptor answer = new TransactionInterceptor(transactionTemplate);
        answer.setProcessor(processor);
        return answer;
    }

    public TransactionTemplate getTemplate() {
        return template;
    }

    public void setTemplate(TransactionTemplate template) {
        this.template = template;
    }
}
