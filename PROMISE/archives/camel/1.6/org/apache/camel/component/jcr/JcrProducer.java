package org.apache.camel.component.jcr;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;

public class JcrProducer extends DefaultProducer<DefaultExchange> {

    public JcrProducer(JcrEndpoint jcrEndpoint) throws LoginException,
            RepositoryException {
        super(jcrEndpoint);
    }

    public void process(Exchange exchange) throws Exception {
        Session session = openSession();
        try {
            Node base = getBaseNode(session);
            Node node = base.addNode(getNodeName(exchange));
            TypeConverter converter = exchange.getContext().getTypeConverter();
            for (String key : exchange.getProperties().keySet()) {
                Value value = converter.convertTo(Value.class, 
                    exchange, exchange.getProperty(key));
                node.setProperty(key, value);
            }
            node.addMixin("mix:referenceable");
            session.save();
            exchange.getOut().setBody(node.getUUID());
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
    }

    private String getNodeName(Exchange exchange) {
        if (exchange.getProperty(JcrComponent.NODE_NAME) != null) {
            return exchange.getProperty(JcrComponent.NODE_NAME).toString();
        }
        return exchange.getExchangeId();
    }

    private Node getBaseNode(Session session) throws Exception {
        Node baseNode = session.getRootNode();
        for (String node : getJcrEndpoint().getBase().split("/")) {
            baseNode = baseNode.addNode(node);
        }
        return baseNode;
    }

    protected Session openSession() throws LoginException, RepositoryException {
        return getJcrEndpoint().getRepository().login(getJcrEndpoint().getCredentials());
    }

    private JcrEndpoint getJcrEndpoint() {
        JcrEndpoint endpoint = (JcrEndpoint) getEndpoint();
        return endpoint;
    }
}
