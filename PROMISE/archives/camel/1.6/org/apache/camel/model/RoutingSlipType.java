package org.apache.camel.model;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.processor.RoutingSlip;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;routingSlip/&gt; element
 */
@XmlRootElement(name = "routingSlip")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoutingSlipType extends ProcessorType<ProcessorType> {
    public static final String ROUTING_SLIP_HEADER = "routingSlipHeader";
    public static final String DEFAULT_DELIMITER = ",";

    @XmlAttribute
    private String headerName;
    @XmlAttribute
    private String uriDelimiter;

    public RoutingSlipType() {
        this(ROUTING_SLIP_HEADER, DEFAULT_DELIMITER);
    }

    public RoutingSlipType(String headerName) {
        this(headerName, DEFAULT_DELIMITER);
    }

    public RoutingSlipType(String headerName, String uriDelimiter) {
        setHeaderName(headerName);
        setUriDelimiter(uriDelimiter);
    }

    @Override
    public String toString() {
        return "RoutingSlip[headerName=" + getHeaderName() + ", uriDelimiter=" + getUriDelimiter() + "]";
    }

    @Override
    public String getShortName() {
        return "routingSlip";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return new RoutingSlip(getHeaderName(), getUriDelimiter());
    }

    @Override
    public List<ProcessorType<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public void setUriDelimiter(String uriDelimiter) {
        this.uriDelimiter = uriDelimiter;
    }

    public String getUriDelimiter() {
        return uriDelimiter;
    }
}
