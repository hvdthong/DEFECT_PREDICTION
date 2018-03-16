package org.apache.camel.component.pojo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

/**
 * @version $Revision: 519901 $
 */
public class PojoExchange extends DefaultExchange {

    public PojoExchange(CamelContext context) {
        super(context);
    }

    public PojoInvocation getInvocation() {
        return getIn().getBody(PojoInvocation.class);
    }

    public void setInvocation(PojoInvocation invocation) {
        getIn().setBody(invocation);
    }

    @Override
    public Exchange newInstance() {
        return new PojoExchange(getContext());
    }
}
