package org.apache.camel.processor.interceptor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.stream.StreamCache;
import org.apache.camel.model.InterceptorRef;
import org.apache.camel.model.InterceptorType;
import org.apache.camel.processor.Interceptor;

/**
 * {@link Interceptor} that converts a message into a re-readable format
 */
public class StreamCachingInterceptor extends Interceptor {

    public StreamCachingInterceptor() {
        super();
        setInterceptorLogic(new Processor() {
            public void process(Exchange exchange) throws Exception {
                Object newBody = exchange.getIn().getBody(StreamCache.class);
                if (newBody != null) {
                    exchange.getIn().setBody(newBody);
                }
                proceed(exchange);
            }
        });
    }

    public StreamCachingInterceptor(Processor processor) {
        this();
        setProcessor(processor);
    }

    @Override
    public String toString() {
        return "StreamCachingInterceptor";
    }

    /**
     * Remove the {@link StreamCachingInterceptor} type of interceptor from the given list of interceptors
     *
     * @param interceptors the list of interceptors
     */
    public static void noStreamCaching(List<InterceptorType> interceptors) {
        for (int i = 0; i < interceptors.size(); i++) {
            InterceptorType interceptor = interceptors.get(i);
            if (interceptor instanceof InterceptorRef
                && ((InterceptorRef)interceptor).getInterceptor() instanceof StreamCachingInterceptor) {
                interceptors.remove(interceptor);
            }
        }
    }
}
