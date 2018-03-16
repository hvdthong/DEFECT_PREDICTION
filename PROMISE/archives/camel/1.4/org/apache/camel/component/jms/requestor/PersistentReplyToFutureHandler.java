package org.apache.camel.component.jms.requestor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.jms.ExceptionListener;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsProducer;
import org.apache.camel.component.jms.requestor.DeferredRequestReplyMap.DeferredMessageSentCallback;
import org.apache.camel.component.jms.requestor.PersistentReplyToRequestor.MessageSelectorComposer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.transaction.PlatformTransactionManager;

public class PersistentReplyToFutureHandler extends FutureHandler {

    private static final transient Log LOG = LogFactory.getLog(PersistentReplyToFutureHandler.class);
    protected PersistentReplyToRequestor requestor;
    protected DeferredMessageSentCallback callback;
    protected String correlationID;

    public PersistentReplyToFutureHandler(PersistentReplyToRequestor requestor,
                                          String correlationID) {
        super();
        this.requestor = requestor;
        this.correlationID = correlationID;
    }

    public PersistentReplyToFutureHandler(PersistentReplyToRequestor requestor,
                                          DeferredMessageSentCallback callback) {
        super();
        this.requestor = requestor;
        this.callback = callback;
    }

    @Override
    public Message get() throws InterruptedException, ExecutionException {
        Message result = null;
        try {
            updateSelector();
            result = super.get();
        } finally {
            revertSelector();
        }
        return result;
    }

    @Override
    public Message get(long timeout, TimeUnit unit) throws InterruptedException,
                                                           ExecutionException,
                                                           TimeoutException {
        Message result = null;
        try {
            updateSelector();
            result = super.get(timeout, unit);
        } finally {
            revertSelector();
        }
        return result;
    }

    protected void updateSelector() throws ExecutionException {
        try {
            MessageSelectorComposer composer = (MessageSelectorComposer)requestor.getListenerContainer();
            composer.addCorrelationID((correlationID != null) ? correlationID : callback.getMessage().getJMSMessageID());
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    protected void revertSelector() throws ExecutionException {
        try {
            MessageSelectorComposer composer = (MessageSelectorComposer)requestor.getListenerContainer();
            composer.removeCorrelationID((correlationID != null) ? correlationID : callback.getMessage().getJMSMessageID());
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }
}
