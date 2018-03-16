package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.model.ExceptionType;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.1 $
 */
public abstract class ErrorHandlerSupport extends ServiceSupport implements ErrorHandler {
    private Map<Class, ExceptionType> exceptionPolicices = new IdentityHashMap<Class, ExceptionType>();

    public void addExceptionPolicy(ExceptionType exception) {
        Processor processor = exception.getErrorHandler();
        addChildService(processor);

        List<Class> list = exception.getExceptionClasses();

        for (Class exceptionType : list) {
            exceptionPolicices.put(exceptionType, exception);
        }
    }

    /**
     * Attempts to invoke the handler for this particular exception if one is available
     *
     * @param exchange
     * @param exception
     * @return
     */
    protected boolean customProcessorForException(Exchange exchange, Throwable exception) throws Exception {
        ExceptionType policy = getExceptionPolicy(exchange, exception);
        Processor processor = policy.getErrorHandler();
        if (processor != null) {
            processor.process(exchange);
            return true;
        }
        return false;
    }

    protected ExceptionType getExceptionPolicy(Exchange exchange, Throwable exception) {
        Set<Map.Entry<Class, ExceptionType>> entries = exceptionPolicices.entrySet();
        for (Map.Entry<Class, ExceptionType> entry : entries) {
            Class type = entry.getKey();
            if (type.isInstance(exception)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
