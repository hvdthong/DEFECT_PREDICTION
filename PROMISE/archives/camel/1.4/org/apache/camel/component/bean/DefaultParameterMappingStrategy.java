package org.apache.camel.component.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.builder.ExpressionBuilder;

/**
 * Represents the strategy used to figure out how to map a message exchange to a POJO method invocation
 *
 * @version $Revision: 630591 $
 */
public class DefaultParameterMappingStrategy implements ParameterMappingStrategy {
    private Map<Class, Expression> parameterTypeToExpressionMap = new ConcurrentHashMap<Class, Expression>();

    public DefaultParameterMappingStrategy() {
        loadDefaultRegistry();
    }

    public synchronized Expression getDefaultParameterTypeExpression(Class parameterType) {
        return parameterTypeToExpressionMap.get(parameterType);
    }

    /**
     * Adds a default parameter type mapping to an expression
     */
    public synchronized void addParameterMapping(Class parameterType, Expression expression) {
        parameterTypeToExpressionMap.put(parameterType, expression);
    }

    /**
     * Creates an invocation on the given POJO using annotations to decide which method to invoke
     * and to figure out which parameters to use
     */
/*
    public MethodInvocation createInvocation(Object pojo,
                                             BeanInfo beanInfo, 
                                             Exchange messageExchange,
                                             Endpoint pojoEndpoint) throws RuntimeCamelException {
        return beanInfo.createInvocation(pojo, messageExchange);
    }
*/
    public void loadDefaultRegistry() {
        addParameterMapping(Exchange.class, ExpressionBuilder.exchangeExpression());
        addParameterMapping(Message.class, ExpressionBuilder.inMessageExpression());
    }
}
