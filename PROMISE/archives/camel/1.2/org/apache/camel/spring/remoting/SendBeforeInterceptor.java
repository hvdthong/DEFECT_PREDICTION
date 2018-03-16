package org.apache.camel.spring.remoting;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.Producer;
import org.apache.camel.component.bean.CamelInvocationHandler;
import org.apache.camel.util.CamelContextHelper;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * A Spring interceptor which sends a message exchange to an endpoint before the method is invoked
 * 
 * @version $Revision: $
 */
public class SendBeforeInterceptor implements MethodInterceptor, CamelContextAware, InitializingBean, DisposableBean {
    private String uri;
    private CamelContext camelContext;
    private CamelInvocationHandler invocationHandler;
    private Producer producer;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        invocationHandler.invoke(invocation.getThis(), invocation.getMethod(), invocation.getArguments());
        return invocation.proceed();
    }

    public void afterPropertiesSet() throws Exception {
        notNull(uri, "uri");
        notNull(camelContext, "camelContext");

        Endpoint endpoint = CamelContextHelper.getMandatoryEndpoint(camelContext, uri);
        producer = endpoint.createProducer();
        producer.start();
        invocationHandler = new CamelInvocationHandler(endpoint, producer);
    }

    public void destroy() throws Exception {
        if (producer != null) {
            producer.stop();
        }
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
