package org.apache.camel.component.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.camel.Exchange;

public class BeanInvocation {

    private final Object proxy;
    private final Method method;
    private final Object[] args;

    public BeanInvocation(Object proxy, Method method, Object[] args) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public Method getMethod() {
        return method;
    }

    public Object getProxy() {
        return proxy;
    }

    /**
     * This causes us to invoke the endpoint Pojo using reflection.
     * 
     * @param pojo
     */
    public void invoke(Object pojo, Exchange exchange) {
        try {
            Object response = getMethod().invoke(pojo, getArgs());
            exchange.getOut().setBody(response);
        } catch (InvocationTargetException e) {
            exchange.setException(e.getCause());
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
