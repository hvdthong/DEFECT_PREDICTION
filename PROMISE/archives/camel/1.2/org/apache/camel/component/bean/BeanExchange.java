package org.apache.camel.component.bean;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

/**
 * @version $Revision: 519901 $
 */
public class BeanExchange extends DefaultExchange {

    public BeanExchange(CamelContext context, ExchangePattern pattern) {
        super(context, pattern);
    }

    public BeanInvocation getInvocation() {
        return getIn().getBody(BeanInvocation.class);
    }

    public void setInvocation(BeanInvocation invocation) {
        getIn().setBody(invocation);
    }

    @Override
    public Exchange newInstance() {
        return new BeanExchange(getContext(), getPattern());
    }
}
