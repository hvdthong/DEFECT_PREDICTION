package org.apache.camel.spring.util;

import org.apache.camel.Expression;

/**
 * A strategy for invoking a method on a pojo from a message exchange
 *
 * @version $Revision: $
 */
public interface MethodInvocationStrategy {
    /**
     * Creates an invocation on the given POJO using annotations to decide which method to invoke
     * and to figure out which parameters to use
     */
/*    MethodInvocation createInvocation(Object pojo,
                                      BeanInfo beanInfo,
                                      Exchange messageExchange,
                                      Endpoint pojoEndpoint) throws RuntimeCamelException;*/

    Expression getDefaultParameterTypeExpression(Class parameterType);
}
