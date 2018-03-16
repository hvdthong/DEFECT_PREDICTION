package org.apache.camel.component.jms.requestor;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * A {@link FutureTask} which implements {@link ReplyHandler}
 * so that it can be used as a handler for a correlation ID
 *
 * @version $Revision: 653015 $
 */
public class FutureHandler extends FutureTask<Message> implements ReplyHandler {
    
    private static final Callable<Message> EMPTY_CALLABLE = new Callable<Message>() {
        public Message call() throws Exception {
            return null;
        }
    };

    public FutureHandler() {
        super(EMPTY_CALLABLE);
    }

    public synchronized void set(Message result) {
        super.set(result);
    }

    public boolean handle(Message message) throws JMSException {
        set(message);
        return true;
    }
}
