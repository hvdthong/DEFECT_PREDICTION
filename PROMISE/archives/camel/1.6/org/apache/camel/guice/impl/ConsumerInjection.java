package org.apache.camel.guice.impl;

import java.lang.reflect.Method;

import com.google.common.base.Objects;
import com.google.inject.Inject;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.ConstructorInvocation;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.CamelPostProcessorHelper;

/**
 * @version $Revision: 724293 $
 */
public class ConsumerInjection extends CamelPostProcessorHelper implements ConstructorInterceptor {
    public Object construct(ConstructorInvocation invocation) throws Throwable {
        Object object = invocation.proceed();
        if (object != null) {
            Class<?> type = object.getClass();
            Method[] methods = type.getMethods();
            for (Method method : methods) {
                consumerInjection(method, object);
            }
        }
        return object;

    }

    @Override
    public CamelContext getCamelContext() {
        CamelContext context = super.getCamelContext();
        Objects.nonNull(context, "CamelContext not injected!");
        return context;
    }

    @Inject
    @Override
    public void setCamelContext(CamelContext camelContext) {
        super.setCamelContext(camelContext);
    }
}
