package org.apache.camel.component.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.ServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link Processor} which converts the inbound exchange to a method
 * invocation on a POJO
 *
 * @version $Revision: 704061 $
 */
public class BeanProcessor extends ServiceSupport implements Processor {
    public static final String METHOD_NAME = "org.apache.camel.MethodName";
    public static final String MULTI_PARAMETER_ARRAY = "org.apache.camel.MultiParameterArray";
    private static final transient Log LOG = LogFactory.getLog(BeanProcessor.class);

    private boolean multiParameterArray;
    private Method methodObject;
    private String method;
    private BeanHolder beanHolder;

    public BeanProcessor(Object pojo, BeanInfo beanInfo) {
        this(new ConstantBeanHolder(pojo, beanInfo));
    }

    public BeanProcessor(Object pojo, CamelContext camelContext, ParameterMappingStrategy parameterMappingStrategy) {
        this(pojo, new BeanInfo(camelContext, pojo.getClass(), parameterMappingStrategy));
    }

    public BeanProcessor(Object pojo, CamelContext camelContext) {
        this(pojo, camelContext, BeanInfo.createParameterMappingStrategy(camelContext));
    }

    public BeanProcessor(BeanHolder beanHolder) {
        this.beanHolder = beanHolder;
    }

    @Override
    public String toString() {
        String description = methodObject != null ? " " + methodObject : "";
        return "BeanProcessor[" + beanHolder + description + "]";
    }

    public void process(Exchange exchange) throws Exception {
        Object bean = beanHolder.getBean();
        exchange.setProperty("org.apache.camel.bean.BeanHolder", beanHolder);

        Processor processor = getProcessor();
        BeanInfo beanInfo = beanHolder.getBeanInfo();

        if (processor != null) {
            processor.process(exchange);
            return;
        }
        Message in = exchange.getIn();

        if (in.getHeader(MULTI_PARAMETER_ARRAY) == null) {
            in.setHeader(MULTI_PARAMETER_ARRAY, isMultiParameterArray());
        }

        try {
            BeanInvocation beanInvoke = in.getBody(BeanInvocation.class);
            if (beanInvoke != null) {
                beanInvoke.invoke(bean, exchange);
                return;
            }
        } catch (NoTypeConversionAvailableException ex) {
        }

        boolean isExplicitMethod = false;
        String prevMethod = null;
        MethodInvocation invocation;
        if (methodObject != null) {
            invocation = beanInfo.createInvocation(methodObject, bean, exchange);
        } else {
            if (ObjectHelper.isNotNullAndNonEmpty(method)) {
                prevMethod = in.getHeader(METHOD_NAME, String.class);
                in.setHeader(METHOD_NAME, method);
                isExplicitMethod = true;
            }
            invocation = beanInfo.createInvocation(bean, exchange);
        }
        if (invocation == null) {
            throw new IllegalStateException(
                "No method invocation could be created, no maching method could be found on: " + bean);
        }
        try {
            Object value = invocation.proceed();
            if (value != null) {
                if (exchange.getPattern().isOutCapable()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Setting bean invocation result on the OUT message: " + value);
                    }
                    exchange.getOut(true).setBody(value);
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Setting bean invocation result on the IN message: " + value);
                    }
                    exchange.getIn().setBody(value);
                }
            }
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof Exception) {
                Exception exception = (Exception)throwable;
                throw exception;
            } else {
                Error error = (Error)throwable;
                throw error;
            }
        } finally {
            if (isExplicitMethod) {
                in.setHeader(METHOD_NAME, prevMethod);
            }
        }
    }

    protected Processor getProcessor() {
        return beanHolder.getProcessor();
    }


    public Method getMethodObject() {
        return methodObject;
    }

    public void setMethodObject(Method methodObject) {
        this.methodObject = methodObject;
    }

    public String getMethod() {
        return method;
    }

    public boolean isMultiParameterArray() {
        return multiParameterArray;
    }

    public void setMultiParameterArray(boolean mpArray) {
        multiParameterArray = mpArray;
    }

    /**
     * Sets the method name to use
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Kept around for backwards compatibility, please use {@link #setMethod(String)}
     * in future instead.
     *
     * @deprecated use {@link #setMethod(String)}. Will be removed in Camel 2.0.
     */
    @Deprecated
    public void setMethodName(String method) {
        setMethod(method);
    }

    protected void doStart() throws Exception {
        ServiceHelper.startService(getProcessor());
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopService(getProcessor());
    }
}
