package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;

/**
 * Represents an XML &lt;inOut/&gt; element
 *
 * @version $Revision: 725680 $
 */
@XmlRootElement(name = "inOut")
@XmlAccessorType(XmlAccessType.FIELD)
public class InOutType extends SendType<InOutType> {

    public InOutType() {
    }

    public InOutType(String uri) {
        setUri(uri);
    }

    public InOutType(Endpoint endpoint) {
        setEndpoint(endpoint);
    }

    @Override
    public String toString() {
        return "InOut[" + getLabel() + "]";
    }

    @Override
    public String getShortName() {
        return "inOut";
    }

    @Override
    public ExchangePattern getPattern() {
        return ExchangePattern.InOut;
    }
}
