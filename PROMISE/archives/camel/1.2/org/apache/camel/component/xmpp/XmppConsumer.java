package org.apache.camel.component.xmpp;

import java.util.Iterator;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;

/**
 * A {@link Consumer} which listens to XMPP packets
 * 
 * @version $Revision: 563665 $
 */
public class XmppConsumer extends DefaultConsumer<XmppExchange> implements PacketListener {
    private static final transient Log LOG = LogFactory.getLog(XmppConsumer.class);
    private final XmppEndpoint endpoint;

    public XmppConsumer(XmppEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        endpoint.getConnection().addPacketListener(this, endpoint.getFilter());
    }

    @Override
    protected void doStop() throws Exception {
        endpoint.getConnection().removePacketListener(this);
        super.doStop();
    }

    public void processPacket(Packet packet) {

        if (packet instanceof Message) {
            Message message = (Message)packet;
            if (LOG.isDebugEnabled()) {
                LOG.debug("<<<< message: " + message.getBody());
            }
            XmppExchange exchange = endpoint.createExchange(message);
            try {
                getProcessor().process(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (packet instanceof RosterPacket) {
            RosterPacket rosterPacket = (RosterPacket)packet;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Roster packet with : " + rosterPacket.getRosterItemCount() + " item(s)");
                Iterator rosterItems = rosterPacket.getRosterItems();
                while (rosterItems.hasNext()) {
                    Object item = rosterItems.next();
                    LOG.debug("Roster item: " + item);
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("<<<< ignored packet: " + packet);
            }

        }
    }
}
