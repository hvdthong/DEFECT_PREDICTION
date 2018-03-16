package org.apache.camel.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.camel.Exchange;

/**
 * Convenience class for holding an {@link Exchange} in a thread-safe way
 */
@SuppressWarnings("serial")
public class AtomicExchange extends AtomicReference<Exchange> {

}
