package org.apache.camel.component.cxf;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.Destination;

/**
 * An {@link Exchange} for working with Apache CXF which exposes the underlying
 * CXF messages via {@link #getInMessage()} and {@link #getOutMessage()} along with the
 * {@link #getExchange()}
 *
 * @version $Revision: 723331 $
 */
public class CxfExchange extends DefaultExchange {
    public static final String DATA_FORMAT = "DATA_FORMAT";
    private Exchange exchange;

    public CxfExchange(CamelContext context, ExchangePattern pattern, Exchange exchange) {
        super(context, pattern);
        this.exchange = exchange;
        if (exchange != null) {
            if (exchange.getOutMessage() != null) {
                setOut(new CxfMessage(exchange.getOutMessage()));
            }
            if (exchange.getInMessage() != null) {
                setIn(new CxfMessage(exchange.getInMessage()));
            }
            if (exchange.getInFaultMessage() != null) {
                setFault(new CxfMessage(exchange.getInFaultMessage()));
            }
        }
    }

    public CxfExchange(CamelContext context, ExchangePattern pattern) {
        super(context, pattern);
    }
    
    public CxfExchange(CxfExchange exchange) {
        super(exchange);
        this.exchange = exchange.getExchange();      
    }

    public CxfExchange(CamelContext context, ExchangePattern pattern, Message inMessage) {
        this(context, pattern);
        this.exchange = inMessage.getExchange();

        setIn(new CxfMessage(inMessage));
        if (exchange != null) {
            if (exchange.getOutMessage() != null) {
                setOut(new CxfMessage(exchange.getOutMessage()));
            }
            if (exchange.getInFaultMessage() != null) {
                setFault(new CxfMessage(exchange.getInFaultMessage()));
            }
        }
    }

    @Override
    public org.apache.camel.Exchange newInstance() {
        return new CxfExchange(this);
    }

    @Override
    public CxfMessage getIn() {
        return (CxfMessage) super.getIn();
    }

    @Override
    public CxfMessage getOut() {
        return (CxfMessage) super.getOut();
    }

    @Override
    public CxfMessage getOut(boolean lazyCreate) {
        return (CxfMessage) super.getOut(lazyCreate);
    }

    @Override
    public CxfMessage getFault() {
        return (CxfMessage) super.getFault();
    }

    @Override
    protected org.apache.camel.Message createFaultMessage() {
        return new CxfMessage();
    }


    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }


    /**
     * Returns the underlying CXF message exchange for an inbound exchange
     * or null for outbound messages
     *
     * @return the inbound message exchange
     */
    public Exchange getExchange() {
        return exchange;
    }

    public Message getInMessage() {
        return getIn().getMessage();
    }

    public Message getOutMessage() {
        return getOut().getMessage();
    }

    public Message getOutFaultMessage() {
        return getExchange().getOutFaultMessage();
    }

    public Message getInFaultMessage() {
        return getExchange().getInFaultMessage();
    }

    public Destination getDestination() {
        return getExchange().getDestination();
    }

    public Conduit getConduit(Message message) {
        return getExchange().getConduit(message);
    }

    @Override
    protected CxfMessage createInMessage() {
        return new CxfMessage();
    }

    @Override
    protected CxfMessage createOutMessage() {
        return new CxfMessage();
    }
}
