package org.apache.camel.converter.jaxb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Message;

/**
 * Represents a JAXB2 representation of a Camel {@link Message} - <b>Important</b>: work in progress!
 *
 * @version $Revision: 664343 $
 */
@XmlRootElement(name = "message")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class MessageType {
    @XmlAnyElement(lax = true)
    @XmlMixed
    List<HeaderType> headers = new ArrayList<HeaderType>();
    @XmlAnyElement(lax = true)
    @XmlMixed
    private List content = new ArrayList();
    @XmlTransient
    private Object body;

    public Object getBody() {
        if (body == null) {
            if (content != null) {
                if (content.size() == 1) {
                    return content.get(0);
                } else {
                    return content;
                }
            }
        }
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
        if (body instanceof List) {
            content = (List)body;
        } else {
            content = new ArrayList();
            content.add(body);
        }
    }

    public List<HeaderType> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderType> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getHeaderMap() {
        Map<String, Object> answer = new HashMap<String, Object>();
        for (HeaderType header : headers) {
            answer.put(header.getName(), header.getValue());
        }
        return answer;
    }

    /**
     * Copies the headers and body of this object from the given Camel message
     *
     * @param message the Camel message to read the headers and body from
     */
    public void copyFrom(Message message) {
        headers.clear();
        Set<Map.Entry<String, Object>> entries = message.getHeaders().entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            if (value != null) {
                headers.add(createHeader(entry.getKey(), value));
            }
        }
        setBody(message.getBody());
    }

    /**
     * Copies the headers and body of this object to the given Camel message
     *
     * @param message the camel message to overwrite its headers and body
     */
    public void copyTo(Message message) {
        message.setHeaders(getHeaderMap());
        message.setBody(getBody());
    }

    protected HeaderType createHeader(String key, Object value) {
        if (value instanceof String) {
            return new StringHeader(key, (String)value);
        } else if (value instanceof Integer) {
            return new IntegerHeader(key, (Integer)value);
        } else if (value instanceof Long) {
            return new LongHeader(key, (Long)value);
        } else {
            return new StringHeader(key, value.toString());

        }
    }
}
