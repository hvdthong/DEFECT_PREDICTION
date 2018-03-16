package org.apache.camel.spring.spi;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.processor.DelayPolicy;
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

import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;

/**
 * EIP pattern.
 *
 * @version $Revision: 695721 $
 */
public class TransactionInterceptor extends DelegateProcessor {
    public static final ExchangeProperty<Boolean> TRANSACTED =
        new ExchangeProperty<Boolean>("transacted", "org.apache.camel.transacted", Boolean.class);
    private static final transient Log LOG = LogFactory.getLog(TransactionInterceptor.class);
    private final TransactionTemplate transactionTemplate;
    private RedeliveryPolicy redeliveryPolicy;
    private DelayPolicy delayPolicy;

    public TransactionInterceptor(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public TransactionInterceptor(Processor processor, TransactionTemplate transactionTemplate) {
        super(processor);
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * @deprecated use DelayPolicy. Will be removed in Camel 2.0
     */
    public TransactionInterceptor(Processor processor, TransactionTemplate transactionTemplate, RedeliveryPolicy redeliveryPolicy) {
        this(processor, transactionTemplate);
        this.redeliveryPolicy = redeliveryPolicy;
        this.delayPolicy = redeliveryPolicy;
    }

    public TransactionInterceptor(Processor processor, TransactionTemplate transactionTemplate, DelayPolicy delayPolicy) {
        this(processor, transactionTemplate);
        this.delayPolicy = delayPolicy;
    }

    @Override
    public String toString() {
        return "TransactionInterceptor:"
            + propagationBehaviorToString(transactionTemplate.getPropagationBehavior())
            + "[" + getProcessor() + "]";
    }

    public void process(final Exchange exchange) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                RuntimeCamelException rce = null;

                boolean activeTx = false;
                try {
                    activeTx = TransactionSynchronizationManager.isActualTransactionActive();
                    if (!activeTx) {
                        activeTx = status.isNewTransaction() && !status.isCompleted();
                        if (!activeTx) {
                            if (DefaultTransactionStatus.class.isAssignableFrom(status.getClass())) {
                                DefaultTransactionStatus defStatus =
                                        DefaultTransactionStatus.class.cast(status);
                                activeTx = defStatus.hasTransaction() && !status.isCompleted();
                            }
                        }
                    }
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Is actual transaction active: " + activeTx);
                    }

                    if (activeTx) {
                        TRANSACTED.set(exchange, Boolean.TRUE);
                    }

                    processNext(exchange);

                    if (exchange.getException() != null) {
                        rce = wrapRuntimeCamelException(exchange.getException());
                    }
                } catch (Exception e) {
                    rce = wrapRuntimeCamelException(e);
                }

                if (rce != null) {
                    delayBeforeRedelivery();
                    if (activeTx) {
                        status.setRollbackOnly();
                        LOG.debug("Setting transaction to rollbackOnly due to exception being thrown: " + rce.getMessage());
                    }
                    throw rce;
                }
            }
        });
    }

    /**
     * Sleeps before the transaction is set as rollback and the caused exception is rethrown to let the
     * Spring TransactionManager handle the rollback.
     */
    protected void delayBeforeRedelivery() {
        long delay = 0;
        if (redeliveryPolicy != null) {
            delay = redeliveryPolicy.getDelay();
        } else if (delayPolicy != null) {
            delay = delayPolicy.getDelay();
        }

        if (delay > 0) {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sleeping for: " + delay + " millis until attempting redelivery");
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Thread interrupted: " + e, e);
                }
            }
        }
    }

    /**
     * @deprecated use DelayPolicy. Will be removed in Camel 2.0
     */
    public RedeliveryPolicy getRedeliveryPolicy() {
        return redeliveryPolicy;
    }

    /**
     * @deprecated use DelayPolicy. Will be removed in Camel 2.0
     */
    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicy = redeliveryPolicy;
    }

    public DelayPolicy getDelayPolicy() {
        return delayPolicy;
    }

    public void setDelayPolicy(DelayPolicy delayPolicy) {
        this.delayPolicy = delayPolicy;
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
