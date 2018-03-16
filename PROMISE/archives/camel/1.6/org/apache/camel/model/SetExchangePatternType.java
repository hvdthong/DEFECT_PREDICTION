package org.apache.camel.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.processor.ExchangePatternProcessor;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;setExchangePattern/&gt; element
 *
 * @version $Revision: 727389 $
 */
@XmlRootElement(name = "setExchangePattern")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetExchangePatternType extends OutputType {
    @XmlAttribute(required = true)
    private ExchangePattern pattern;
    @XmlTransient
    private ExchangePatternProcessor processor;
    
    public SetExchangePatternType() {
    }

    public SetExchangePatternType(ExchangePattern pattern) {
        this.pattern = pattern;
    }

    public SetExchangePatternType pattern(ExchangePattern pattern) {
        setPattern(pattern);
        return this;
    }

    public ExchangePattern getPattern() {
        return pattern;
    }

    public void setPattern(ExchangePattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getShortName() {
        return "setExchangePattern";
    }

    @Override
    public String toString() {
        return "setExchangePattern["
                + "pattern: " + pattern
                + "]";
    }

    @Override
    public String getLabel() {
        return "setExchangePattern: " + pattern;
    }
   
    @Override
    public Processor createProcessor(RouteContext routeContext) {
        if (processor == null) {
            processor = new ExchangePatternProcessor(getPattern());
        }
        return processor;
    }

}
