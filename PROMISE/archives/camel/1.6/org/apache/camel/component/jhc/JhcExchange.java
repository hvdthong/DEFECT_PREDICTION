package org.apache.camel.component.jhc;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

public class JhcExchange extends DefaultExchange {

    public JhcExchange(CamelContext context) {
        super(context);
    }

    public JhcExchange(CamelContext context, ExchangePattern pattern) {
        super(context, pattern);
    }

}
