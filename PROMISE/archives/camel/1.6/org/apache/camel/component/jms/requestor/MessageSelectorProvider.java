package org.apache.camel.component.jms.requestor;

import java.util.HashMap;
import java.util.Map;

public class MessageSelectorProvider {
    protected Map<String, String> correlationIds;
    protected boolean dirty = true;
    protected StringBuilder expression;

    public MessageSelectorProvider() {
        correlationIds = new HashMap<String, String>();
    }

    public synchronized void addCorrelationID(String id) {
        correlationIds.put(id, id);
        dirty = true;
    }

    public synchronized void removeCorrelationID(String id) {
        correlationIds.remove(id);
        dirty = true;
    }

    public synchronized String get() {
        if (!dirty) {
            return expression.toString();
        }
        expression = new StringBuilder("JMSCorrelationID='");
        boolean first = true;
        for (Map.Entry<String, String> entry : correlationIds.entrySet()) {
            if (!first) {
                expression.append(" OR JMSCorrelationID='");
            }
            expression.append(entry.getValue()).append("'");
            if (first) {
                first = false;
            }
        }
        dirty = false;
        return expression.toString();
    }
}
