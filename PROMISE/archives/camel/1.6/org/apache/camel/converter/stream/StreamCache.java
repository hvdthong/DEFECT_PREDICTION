package org.apache.camel.converter.stream;

import java.io.IOException;

import org.apache.camel.processor.interceptor.StreamCachingInterceptor;

/**
 * Tagging interface to indicate that a type is capable of caching the underlying data stream.
 * <p/>
 * This is a useful feature for avoid message re-readability issues.
 * This interface is mainly used by the {@link StreamCachingInterceptor} for determining if/how to wrap a
 * stream-based message.
 */
public interface StreamCache {

    /**
     * Resets the StreamCache for a new stream consumption.
     */
    void reset();

}
