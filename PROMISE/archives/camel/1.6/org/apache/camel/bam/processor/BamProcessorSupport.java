package org.apache.camel.bam.processor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;

/**
 * A base {@link Processor} for working on <a
 * class would do the actual persistence such as the {@link JpaBamProcessor}
 *
 * @version $Revision: 695721 $
 */
public abstract class BamProcessorSupport<T> implements Processor {
    private static final transient Log LOG = LogFactory.getLog(BamProcessorSupport.class);
    private Class<T> entityType;
    private Expression<Exchange> correlationKeyExpression;
    private TransactionTemplate transactionTemplate;
    private int retryCount = 20;
    private long retrySleep = 1000L;

    protected BamProcessorSupport(TransactionTemplate transactionTemplate,
                                  Expression<Exchange> correlationKeyExpression) {
        this.transactionTemplate = transactionTemplate;
        this.correlationKeyExpression = correlationKeyExpression;

        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type[] arguments = parameterizedType.getActualTypeArguments();
            if (arguments.length > 0) {
                Type argumentType = arguments[0];
                if (argumentType instanceof Class) {
                    this.entityType = (Class<T>)argumentType;
                }
            }
        }
        if (entityType == null) {
            throw new IllegalArgumentException("Could not infer the entity type!");
        }
    }

    protected BamProcessorSupport(TransactionTemplate transactionTemplate,
                                  Expression<Exchange> correlationKeyExpression, Class<T> entitytype) {
        this.transactionTemplate = transactionTemplate;
        this.entityType = entitytype;
        this.correlationKeyExpression = correlationKeyExpression;
    }

    public void process(final Exchange exchange) {
        for (int i = 1; i <= retryCount; i++) {
            if (i > 1) {
                LOG.info("Retrying attempt: " + i);
                try {
                    Thread.sleep(retryCount);
                } catch (InterruptedException e) {
                    LOG.debug("Caught: " + e, e);
                }
            }
            try {
                transactionTemplate.execute(new TransactionCallback() {
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            Object key = getCorrelationKey(exchange);

                            T entity = loadEntity(exchange, key);

                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Correlation key: " + key + " with entity: " + entity);
                            }
                            processEntity(exchange, entity);

                            return entity;
                        } catch (Exception e) {
                            return onError(status, e);
                        }
                    }
                });
                if (i > 1) {
                    LOG.info("Attempt " + i + " worked!");
                }
                return;
            } catch (Exception e) {
                LOG.warn("Failed to complete transaction: " + e, e);
            }
        }
    }

    public Expression<Exchange> getCorrelationKeyExpression() {
        return correlationKeyExpression;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    protected abstract void processEntity(Exchange exchange, T entity) throws Exception;

    protected abstract T loadEntity(Exchange exchange, Object key) throws Exception;

    protected abstract Class getKeyType();

    protected Object getCorrelationKey(Exchange exchange) throws NoCorrelationKeyException {
        Object value = correlationKeyExpression.evaluate(exchange);
        Class keyType = getKeyType();
        if (keyType != null) {
            value = ExchangeHelper.convertToType(exchange, keyType, value);
        }
        if (value == null) {
            throw new NoCorrelationKeyException(this, exchange);
        }
        return value;
    }

    protected Object onError(TransactionStatus status, Exception e) {
        status.setRollbackOnly();
        LOG.error("Caught: " + e, e);
        throw wrapRuntimeCamelException(e);
    }

}
