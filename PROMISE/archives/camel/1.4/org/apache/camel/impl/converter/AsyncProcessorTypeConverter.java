package org.apache.camel.impl.converter;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.TypeConverter;
import org.apache.camel.processor.DelegateProcessor;

/**
 * A simple converter that can convert any {@link Processor} to an {@link AsyncProcessor}.
 * Processing will still occur synchronously but it will provide the required
 * notifications that the caller expects.
 * 
 * @version $Revision: 659849 $
 */
public class AsyncProcessorTypeConverter implements TypeConverter {

    private static final class ProcessorToAsyncProcessorBridge extends DelegateProcessor implements AsyncProcessor {

        private ProcessorToAsyncProcessorBridge(Processor processor) {
            super(processor);
        }

        public boolean process(Exchange exchange, AsyncCallback callback) {
            try {
                processor.process(exchange);
            } catch (Throwable e) {
                exchange.setException(e);
            }
            callback.done(true);
            return true;
        }
    }

    public <T> T convertTo(Class<T> toType, Object value) {
        if (value != null) {
            if (toType.equals(AsyncProcessor.class)) {
                if (value instanceof AsyncProcessor) {
                    return toType.cast(value);
                } else if (value instanceof Processor) {
                    final Processor processor = (Processor)value;
                    return toType.cast(new ProcessorToAsyncProcessorBridge(processor));
                }
            }
        }
        return null;
    }

    public static AsyncProcessor convert(Processor value) {
        if (value instanceof AsyncProcessor) {
            return (AsyncProcessor)value;
        }
        return new ProcessorToAsyncProcessorBridge(value);
    }
}
