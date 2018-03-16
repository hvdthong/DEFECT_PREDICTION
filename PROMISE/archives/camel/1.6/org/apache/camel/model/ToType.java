package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;

/**
 * Represents an XML &lt;to/&gt; element
 *
 * @version $Revision: 725675 $
 */
@XmlRootElement(name = "to")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToType extends SendType<ToType> {
    @XmlAttribute(required = false)
    private ExchangePattern pattern;

    public ToType() {
    }

    public ToType(String uri) {
        setUri(uri);
    }

    public ToType(Endpoint endpoint) {
        setEndpoint(endpoint);
    }

    public ToType(String uri, ExchangePattern pattern) {
        this(uri);
        this.pattern = pattern;
    }

    public ToType(Endpoint endpoint, ExchangePattern pattern) {
        this(endpoint);
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "To[" + getLabel() + "]";
    }

    @Override
    public String getShortName() {
        return "to";
    }

    @Override
    public ExchangePattern getPattern() {
        return pattern;
    }

    /**
     * Sets the optional {@link ExchangePattern} used to invoke this endpoint
     */
    public void setPattern(ExchangePattern pattern) {
        this.pattern = pattern;
    }

}
