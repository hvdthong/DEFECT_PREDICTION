package org.apache.camel.processor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.model.ExceptionType;
import org.apache.camel.processor.exceptionpolicy.DefaultExceptionPolicyStrategy;
import org.apache.camel.processor.exceptionpolicy.ExceptionPolicyStrategy;

/**
 * Support class for {@link ErrorHandler} implementations.
 *
 * @version $Revision: 645359 $
 */
public abstract class ErrorHandlerSupport extends ServiceSupport implements ErrorHandler {
    private Map<Class, ExceptionType> exceptionPolicies = new LinkedHashMap<Class, ExceptionType>();
    private ExceptionPolicyStrategy exceptionPolicy = createDefaultExceptionPolicyStrategy();

    public void addExceptionPolicy(ExceptionType exception) {
        Processor processor = exception.getErrorHandler();
        addChildService(processor);

        List<Class> list = exception.getExceptionClasses();

        for (Class exceptionType : list) {
            exceptionPolicies.put(exceptionType, exception);
        }
    }

    /**
     * Attempts to invoke the handler for this particular exception if one is available
     */
    protected boolean customProcessorForException(Exchange exchange, Throwable exception) throws Exception {
        ExceptionType policy = getExceptionPolicy(exchange, exception);
        if (policy != null) {
            Processor processor = policy.getErrorHandler();
            if (processor != null) {
                processor.process(exchange);
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to find the best suited {@link ExceptionType} to be used for handling the given thrown exception.
     *
     * @param exchange  the exchange
     * @param exception the exception that was thrown
     * @return the best exception type to handle this exception, <tt>null</tt> if none found.
     */
    protected ExceptionType getExceptionPolicy(Exchange exchange, Throwable exception) {
        if (exceptionPolicy == null) {
            throw new IllegalStateException("The exception policy has not been set");
        }

        return exceptionPolicy.getExceptionPolicy(exceptionPolicies, exchange, exception);
    }

    /**
     * Sets the strategy to use for resolving the {@link ExceptionType} to use
     * for handling thrown exceptions.
     */
    public void setExceptionPolicy(ExceptionPolicyStrategy exceptionPolicy) {
        this.exceptionPolicy = exceptionPolicy;
    }

    /**
     * Creates the default exception policy strategy to use.
     */
    public static ExceptionPolicyStrategy createDefaultExceptionPolicyStrategy() {
        return new DefaultExceptionPolicyStrategy();
    }

}
