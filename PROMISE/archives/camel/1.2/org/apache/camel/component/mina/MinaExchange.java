package org.apache.camel.component.mina;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

/**
 * A {@link Exchange} for MINA
 * 
 * @version $Revision: 577559 $
 */
public class MinaExchange extends DefaultExchange {

    public MinaExchange(CamelContext camelContext, ExchangePattern pattern) {
        super(camelContext, pattern);
    }
}
