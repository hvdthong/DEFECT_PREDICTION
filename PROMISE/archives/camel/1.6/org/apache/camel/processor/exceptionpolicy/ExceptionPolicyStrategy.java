package org.apache.camel.processor.exceptionpolicy;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.model.ExceptionType;

/**
 * A strategy to determine which {@link org.apache.camel.model.ExceptionType} should handle the thrown
 * exception.
 *
 * @see org.apache.camel.processor.exceptionpolicy.DefaultExceptionPolicyStrategy DefaultExceptionPolicy
 */
public interface ExceptionPolicyStrategy {

    /**
     * Resolves the {@link org.apache.camel.model.ExceptionType} that should handle the thrown exception.
     *
     * @param exceptionPolicices the configured exception policies to resolve from
     * @param exchange           the exchange
     * @param exception          the exception that was thrown
     * @return the resolved exception type to handle this exception, <tt>null</tt> if none found.
     */
    ExceptionType getExceptionPolicy(Map<ExceptionPolicyKey, ExceptionType> exceptionPolicices, Exchange exchange,
                                            Throwable exception);

}
