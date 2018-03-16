package org.apache.camel.component.irc;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;

public class IrcProducer extends DefaultProducer<IrcExchange> {

    public static final String[] COMMANDS = new String[] {"AWAY", "INVITE", "ISON", "JOIN", "KICK", "LIST", "NAMES", "PRIVMSG", "MODE", "NICK", "NOTICE", "PART", "PONG", "QUIT", "TOPIC", "WHO",
                                                          "WHOIS", "WHOWAS", "USERHOST"};
    private static final transient Log LOG = LogFactory.getLog(IrcProducer.class);

    private IRCConnection connection;
    private IrcEndpoint endpoint;
    private IRCEventListener ircErrorLogger;

    public IrcProducer(IrcEndpoint endpoint, IRCConnection connection) {
        super(endpoint);
        this.endpoint = endpoint;
        this.connection = connection;
    }

    public void process(Exchange exchange) throws Exception {
        try {
            final String msg = exchange.getIn().getBody(String.class);
            if (isMessageACommand(msg)) {
                connection.send(msg);
            } else {
                final String target = endpoint.getConfiguration().getTarget();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("sending to: " + target + " message: " + msg);
                }

                connection.doPrivmsg(target, msg);
            }
        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        ircErrorLogger = createIrcErrorLogger();
        connection.addIRCEventListener(ircErrorLogger);

        final String target = endpoint.getConfiguration().getTarget();

        LOG.debug("joining: " + target);
        connection.doJoin(target);
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (connection != null) {
            connection.removeIRCEventListener(ircErrorLogger);
        }
    }

    protected boolean isMessageACommand(String msg) {
        for (String command : COMMANDS) {
            if (msg.startsWith(command)) {
                return true;
            }
        }
        return false;
    }

    protected IRCEventListener createIrcErrorLogger() {
        return new IrcErrorLogger(LOG);
    }

}
