package org.apache.camel.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Allows an element to have an optional ID specified
 *
 * @version $Revision: 672271 $
 */
@XmlType(name = "optionalIdentifiedType")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class OptionalIdentifiedType<T extends OptionalIdentifiedType> {
    @XmlTransient
    protected static Map<String, AtomicInteger> nodeCounters = new HashMap<String, AtomicInteger>();
    @XmlAttribute(required = false)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    private String id;
    @XmlElement(required = false)
    private Description description;


    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * Returns a short name for this node which can be useful for ID generation or referring to related resources like images
     *
     * @return defaults to "node" but derived nodes should overload this to provide a unique name
     */
    public String getShortName() {
        return "node";
    }

    public T description(String text) {
        if (description == null) {
            description = new Description();
        }
        description.setText(text);
        return (T) this;
    }

    public T description(String text, String lang) {
        description(text);
        description.setLang(lang);
        return (T) this;
    }

    public T id(String id) {
        setId(id);
        return (T) this;
    }

    public String idOrCreate() {
        if (id == null) {
            setId(createId());
        }
        return getId();
    }


    /**
     * A helper method to create a new ID for this node
     */
    protected String createId() {
        String key = getShortName();
        return key + getNodeCounter(key).incrementAndGet();
    }

    /**
     * Returns the counter for the given node key, lazily creating one if necessary
     */
    protected static synchronized AtomicInteger getNodeCounter(String key) {
        AtomicInteger answer = nodeCounters.get(key);
        if (answer == null) {
            answer = new AtomicInteger(0);
            nodeCounters.put(key, answer);
        }
        return answer;
    }
}
