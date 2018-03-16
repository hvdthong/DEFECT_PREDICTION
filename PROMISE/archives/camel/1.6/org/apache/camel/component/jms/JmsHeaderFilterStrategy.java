package org.apache.camel.component.jms;


import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.apache.camel.util.ObjectHelper;

/**
 * @version $Revision: 681589 $
 */
public class JmsHeaderFilterStrategy extends DefaultHeaderFilterStrategy {

    public JmsHeaderFilterStrategy() {
        initialize();
    }

    protected void initialize() {
        getOutFilter().add("JMSXUserID");
        getOutFilter().add("JMSXAppID");
        getOutFilter().add("JMSXDeliveryCount");
        getOutFilter().add("JMSXProducerTXID");
        getOutFilter().add("JMSXConsumerTXID");
        getOutFilter().add("JMSXRcvTimestamp");
        getOutFilter().add("JMSXRecvTimestamp");
        getOutFilter().add("JMSXState");
    }

    @Override
    protected boolean extendedFilter(Direction direction, String key, Object value) {
        return Direction.OUT == direction
            && !ObjectHelper.isJavaIdentifier(JmsBinding.encodeToSafeJmsHeaderName(key));
    }
}
