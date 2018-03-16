package org.apache.camel.component.jms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.camel.Exchange;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsOperations;

/**
 * A default implementation of queue browsing using the Spring 2.5.x {@link BrowserCallback}
 * @version $Revision: 647893 $
 */
public class DefaultQueueBrowseStrategy implements QueueBrowseStrategy {

    public List<Exchange> browse(JmsOperations template, String queue, final JmsQueueEndpoint endpoint) {
        return  (List<Exchange>) template.browse(queue, new BrowserCallback() {

            public Object doInJms(Session session, QueueBrowser browser) throws JMSException {

                List<Exchange> answer = new ArrayList<Exchange>();
                Enumeration iter = browser.getEnumeration();
                while (iter.hasMoreElements()) {
                    Message message = (Message) iter.nextElement();
                    JmsExchange exchange = endpoint.createExchange(message);
                    answer.add(exchange);
                }
                return answer;
            }
        });
    }
}
