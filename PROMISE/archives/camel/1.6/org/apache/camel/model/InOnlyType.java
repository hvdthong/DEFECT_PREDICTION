package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;

/**
 * Represents an XML &lt;inOnly/&gt; element
 *
 * @version $Revision: 733652 $
 */
@XmlRootElement(name = "inOnly")
@XmlAccessorType(XmlAccessType.FIELD)
public class InOnlyType extends SendType<InOnlyType> {

    public InOnlyType() {
    }

    public InOnlyType(String uri) {
        setUri(uri);
    }

    public InOnlyType(Endpoint endpoint) {
        setEndpoint(endpoint);
    }

    @Override
    public String toString() {
        return "InOnly[" + getLabel() + "]";
    }

    @Override
    public String getShortName() {
        return "inOnly";
    }

    @Override
    public ExchangePattern getPattern() {
        return ExchangePattern.InOnly;
    }


}
