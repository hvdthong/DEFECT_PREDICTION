package org.apache.camel.component.mina;

import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

/**
 * A {@link Exchange} for MINA
 * 
 * @version $Revision: 520985 $
 */
public class MinaExchange extends DefaultExchange {

    public MinaExchange(CamelContext camelContext) {
        super(camelContext);
    }
}
