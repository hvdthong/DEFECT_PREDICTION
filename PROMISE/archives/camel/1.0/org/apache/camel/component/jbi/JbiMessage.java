package org.apache.camel.component.jbi;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

import javax.jbi.messaging.NormalizedMessage;
import java.util.Iterator;
import java.util.Map;

/**
 * A JBI {@link Message} which provides access to the underlying JBI features such as {@link #getNormalizedMessage()}
 *
 * @version $Revision: 522517 $
 */
public class JbiMessage extends DefaultMessage {
    private NormalizedMessage normalizedMessage;

    public JbiMessage() {
    }

    public JbiMessage(NormalizedMessage normalizedMessage) {
        this.normalizedMessage = normalizedMessage;
    }

    @Override
    public String toString() {
        if (normalizedMessage != null) {
            return "JbiMessage: " + normalizedMessage;
        }
        else {
            return "JbiMessage: " + getBody();
        }
    }

    @Override
    public JbiExchange getExchange() {
        return (JbiExchange) super.getExchange();
    }

    /**
     * Returns the underlying JBI message
     *
     * @return the underlying JBI message
     */
    public NormalizedMessage getNormalizedMessage() {
        return normalizedMessage;
    }

    public void setNormalizedMessage(NormalizedMessage normalizedMessage) {
        this.normalizedMessage = normalizedMessage;
    }

    public Object getHeader(String name) {
        Object answer = null;
        if (normalizedMessage != null) {
            answer = normalizedMessage.getProperty(name);
        }
        if (answer == null) {
            answer = super.getHeader(name);
        }
        return answer;
    }

    @Override
    public JbiMessage newInstance() {
        return new JbiMessage();
    }

    @Override
    protected Object createBody() {
        if (normalizedMessage != null) {
            return getExchange().getBinding().extractBodyFromJbi(getExchange(), normalizedMessage);
        }
        return null;
    }

    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        if (normalizedMessage != null) {
            Iterator iter = normalizedMessage.getPropertyNames().iterator();
            while (iter.hasNext()) {
                String name = iter.next().toString();
                Object value = normalizedMessage.getProperty(name);
                map.put(name, value);
            }
        }
    }
}
