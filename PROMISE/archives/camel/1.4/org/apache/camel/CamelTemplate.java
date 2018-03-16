package org.apache.camel;

import org.apache.camel.impl.DefaultProducerTemplate;

/**
 * A client helper object (named like Spring's TransactionTemplate & JmsTemplate
 * et al) for working with Camel and sending {@link Message} instances in an
 * {@link Exchange} to an {@link Endpoint}.
 *
 * @version $Revision: 663882 $
 * @deprecated use {@link ProducerTemplate} instead, can be created using {@link org.apache.camel.CamelContext#createProducerTemplate()}. Will be removed in Camel 2.0
 */
@Deprecated
public class CamelTemplate<E extends Exchange> extends DefaultProducerTemplate<E> {

    public CamelTemplate(CamelContext context) {
        super(context);
    }

    public CamelTemplate(CamelContext context, Endpoint defaultEndpoint) {
        super(context, defaultEndpoint);
    }
}
