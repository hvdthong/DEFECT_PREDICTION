package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;pipeline/&gt; element which can be used to define an explicit pipeline; or to define
 * a specific pipeline within a &lt;multicast&gt; block
 *
 * @version $Revision: 673835 $
 */
@XmlRootElement(name = "pipeline")
@XmlAccessorType(XmlAccessType.FIELD)
public class PipelineType extends OutputType<ProcessorType> {

    @Override
    public String getShortName() {
        return "pipeline";
    }

    public Processor createProcessor(RouteContext routeContext) throws Exception {
        return createOutputsProcessor(routeContext);
    }
}
