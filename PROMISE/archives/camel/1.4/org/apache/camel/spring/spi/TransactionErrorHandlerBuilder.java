package org.apache.camel.spring.spi;

import org.apache.camel.Processor;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.builder.ErrorHandlerBuilderSupport;
import org.apache.camel.processor.RedeliveryPolicy;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * An error handler which will roll the exception back if there is an error
 * rather than using the dead letter channel and retry logic.
 *
 * A delay is also used after a rollback
 *
 * @version $Revision: 1.1 $
 */
public class TransactionErrorHandlerBuilder extends ErrorHandlerBuilderSupport implements Cloneable, InitializingBean {

    private TransactionTemplate transactionTemplate;
    private RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();

    public TransactionErrorHandlerBuilder() {
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public RedeliveryPolicy getRedeliveryPolicy() {
        return redeliveryPolicy;
    }

    public void setRedeliveryPolicy(RedeliveryPolicy redeliveryPolicy) {
        this.redeliveryPolicy = redeliveryPolicy;
    }

    public ErrorHandlerBuilder copy() {
        try {
            return (ErrorHandlerBuilder) clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("Clone should be supported: " + e, e);
        }
    }

    public Processor createErrorHandler(RouteContext routeContext, Processor processor) throws Exception {
        return new TransactionInterceptor(processor, transactionTemplate, redeliveryPolicy);
    }

    public void afterPropertiesSet() throws Exception {
        ObjectHelper.notNull(transactionTemplate, "transactionTemplate");
    }

    public TransactionErrorHandlerBuilder backOffMultiplier(double backOffMultiplier) {
        getRedeliveryPolicy().backOffMultiplier(backOffMultiplier);
        return this;
    }

    public TransactionErrorHandlerBuilder collisionAvoidancePercent(short collisionAvoidancePercent) {
        getRedeliveryPolicy().collisionAvoidancePercent(collisionAvoidancePercent);
        return this;
    }

    public TransactionErrorHandlerBuilder initialRedeliveryDelay(long initialRedeliveryDelay) {
        getRedeliveryPolicy().initialRedeliveryDelay(initialRedeliveryDelay);
        return this;
    }

    public TransactionErrorHandlerBuilder maximumRedeliveries(int maximumRedeliveries) {
        getRedeliveryPolicy().maximumRedeliveries(maximumRedeliveries);
        return this;
    }

    public TransactionErrorHandlerBuilder maximumRedeliveryDelay(long maximumRedeliveryDelay) {
        getRedeliveryPolicy().maximumRedeliveryDelay(maximumRedeliveryDelay);
        return this;
    }

    public TransactionErrorHandlerBuilder useCollisionAvoidance() {
        getRedeliveryPolicy().useCollisionAvoidance();
        return this;
    }

    public TransactionErrorHandlerBuilder useExponentialBackOff() {
        getRedeliveryPolicy().useExponentialBackOff();
        return this;
    }

}
