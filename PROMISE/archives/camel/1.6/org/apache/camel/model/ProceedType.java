package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;proceed/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "proceed")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProceedType extends OutputType<ProcessorType> {

    @Override
    public String getShortName() {
        return "proceed";
    }

    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return createOutputsProcessor(routeContext);
    }
}
