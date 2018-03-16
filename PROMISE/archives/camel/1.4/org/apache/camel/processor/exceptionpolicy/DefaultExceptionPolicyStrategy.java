package org.apache.camel.processor.exceptionpolicy;

import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.model.ExceptionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default strategy used in Camel to resolve the {@link org.apache.camel.model.ExceptionType} that should
 * handle the thrown exception.
 * <p/>
 * This strategy applies the following rules:
 * <ul>
 *   <li>The exception type must be configured with an Exception that is an instance of the thrown exception</li>
 *   <li>If the exception type has exactly the thrown exception then its selected</li>
 *   <li>Otherwise the type that has an exception that is super of the thrown exception is selected
 *       (recurring up the exception hierarchy)
 *  </ul>
 */
public class DefaultExceptionPolicyStrategy implements ExceptionPolicyStrategy {

    private static final transient Log LOG = LogFactory.getLog(DefaultExceptionPolicyStrategy.class);

    public ExceptionType getExceptionPolicy(Map<Class, ExceptionType> exceptionPolicices, Exchange exchange,
                                            Throwable exception) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finding best suited exception policy for thrown exception " + exception.getClass().getName());
        }

        int targetLevel = getInheritanceLevel(exception.getClass());
        ExceptionType candidate = null;
        int candidateDiff = Integer.MAX_VALUE;

        Set<Map.Entry<Class, ExceptionType>> entries = exceptionPolicices.entrySet();
        for (Map.Entry<Class, ExceptionType> entry : entries) {
            Class clazz = entry.getKey();
            ExceptionType type = entry.getValue();

            if (clazz.isInstance(exception)) {

                if (clazz.equals(exception.getClass())) {
                    candidate = type;
                    break;
                }

                int level = getInheritanceLevel(clazz);
                int diff = targetLevel - level;

                if (diff < candidateDiff) {
                    candidate = type;
                    candidateDiff = diff;
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            if (candidate != null) {
                LOG.debug("Using " + candidate + " as the exception policy");
            } else {
                LOG.debug("No candidate found to be used as exception policy");
            }
        }

        return candidate;
    }

    private static int getInheritanceLevel(Class clazz) {
        if (clazz == null || "java.lang.Object".equals(clazz.getName())) {
            return 0;
        }
        return 1 + getInheritanceLevel(clazz.getSuperclass());
    }

}
