package org.apache.camel.spring.spi;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.processor.RedeliveryPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * EIP pattern.
 *
 * @version $Revision: 674383 $
 */
public class TransactionInterceptor extends DelegateProcessor {
    public static final ExchangeProperty<Boolean> TRANSACTED =
        new ExchangeProperty<Boolean>("transacted", "org.apache.camel.transacted", Boolean.class);
    private static final transient Log LOG = LogFactory.getLog(TransactionInterceptor.class);
    private final TransactionTemplate transactionTemplate;
    private ThreadLocal<RedeliveryData> previousRollback = new ThreadLocal<RedeliveryData>() {
        @Override
        protected RedeliveryData initialValue() {
            return new RedeliveryData();
        }
    };
    private RedeliveryPolicy redeliveryPolicy;

    public TransactionInterceptor(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public TransactionInterceptor(Processor processor, TransactionTemplate transactionTemplate) {
        super(processor);
        this.transactionTemplate = transactionTemplate;
    }

    public TransactionInterceptor(Processor processor, TransactionTemplate transactionTemplate, RedeliveryPolicy redeliveryPolicy) {
        this(processor, transactionTemplate);
        this.redeliveryPolicy = redeliveryPolicy;
    }

    @Override
    public String toString() {
        return "TransactionInterceptor:"
            + propagationBehaviorToString(transactionTemplate.getPropagationBehavior())
            + "[" + getProcessor() + "]";
    }

    public void process(final Exchange exchange) {
        LOG.debug("Transaction begin");

        final RedeliveryData redeliveryData = previousRollback.get();

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (redeliveryPolicy != null && redeliveryData.previousRollback) {
                    redeliveryData.redeliveryDelay = redeliveryPolicy.sleep(redeliveryData.redeliveryDelay);
                }

                RuntimeCamelException rce = null;

                boolean activeTx = false;
                try {
                    activeTx = TransactionSynchronizationManager.isActualTransactionActive();
                    if (!activeTx) {
                        activeTx = status.isNewTransaction() && !status.isCompleted();
                        if (!activeTx) {
                            if (DefaultTransactionStatus.class.isAssignableFrom(status.getClass())) {
                                DefaultTransactionStatus defStatus = DefaultTransactionStatus.class
                                    .cast(status);
                                activeTx = defStatus.hasTransaction() && !status.isCompleted();
                            }
                        }
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Is actual transaction active: " + activeTx);
                    }

                    if (activeTx) {
                        TRANSACTED.set(exchange, Boolean.TRUE);
                    }

                    processNext(exchange);

                    if (exchange.getException() != null) {
                        rce = new RuntimeCamelException(exchange.getException());
                    }
                } catch (Exception e) {
                    rce = new RuntimeCamelException(e);
                }

                if (rce != null) {
                    redeliveryData.previousRollback = true;
                    if (activeTx) {
                        status.setRollbackOnly();
                        LOG.debug("Transaction rollback");
                    }
                    throw rce;
                }
            }
        });

        redeliveryData.previousRollback = false;
        redeliveryData.redeliveryDelay = 0L;

        LOG.debug("Transaction commit");
    }


    public RedeliveryPolicy getRedeliveryPolicy() {
        return redeliveryPolicy;
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicy = redeliveryPolicy;
    }

    protected static class RedeliveryData {
        boolean previousRollback;
        long redeliveryDelay;
    }

    protected String propagationBehaviorToString(int propagationBehavior) {
        String rc;
        switch (propagationBehavior) {
        case TransactionDefinition.PROPAGATION_MANDATORY:
            rc = "PROPAGATION_MANDATORY";
            break;
        case TransactionDefinition.PROPAGATION_NESTED:
            rc = "PROPAGATION_NESTED";
            break;
        case TransactionDefinition.PROPAGATION_NEVER:
            rc = "PROPAGATION_NEVER";
            break;
        case TransactionDefinition.PROPAGATION_NOT_SUPPORTED:
            rc = "PROPAGATION_NOT_SUPPORTED";
            break;
        case TransactionDefinition.PROPAGATION_REQUIRED:
            rc = "PROPAGATION_REQUIRED";
            break;
        case TransactionDefinition.PROPAGATION_REQUIRES_NEW:
            rc = "PROPAGATION_REQUIRES_NEW";
            break;
        case TransactionDefinition.PROPAGATION_SUPPORTS:
            rc = "PROPAGATION_SUPPORTS";
            break;
        default:
            rc = "UNKNOWN";
        }
        return rc;
    }

}
