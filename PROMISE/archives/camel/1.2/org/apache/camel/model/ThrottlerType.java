package org.apache.camel.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.processor.Throttler;

/**
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "throttler")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrottlerType extends ProcessorType {
    @XmlAttribute
    private Long maximumRequestsPerPeriod;
    @XmlAttribute
    private long timePeriodMillis = 1000;
    @XmlElementRef
    private List<InterceptorType> interceptors = new ArrayList<InterceptorType>();
    @XmlElementRef
    private List<ProcessorType> outputs = new ArrayList<ProcessorType>();

    public ThrottlerType() {
    }

    public ThrottlerType(long maximumRequestsPerPeriod) {
        this.maximumRequestsPerPeriod = maximumRequestsPerPeriod;
    }

    @Override
    public String toString() {
        return "Throttler[" + getMaximumRequestsPerPeriod() + " request per " + getTimePeriodMillis()
               + " millis -> " + getOutputs() + "]";
    }

    @Override
    public String getLabel() {
        return "" + getMaximumRequestsPerPeriod() + " per " + getTimePeriodMillis() + " (ms)";
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = routeContext.createProcessor(this);
        return new Throttler(childProcessor, maximumRequestsPerPeriod, timePeriodMillis);
    }


    /**
     * Sets the time period during which the maximum request count is valid for
     */
    public ThrottlerType timePeriodMillis(long timePeriodMillis) {
        this.timePeriodMillis = timePeriodMillis;
        return this;
    }


    public Long getMaximumRequestsPerPeriod() {
        return maximumRequestsPerPeriod;
    }

    public void setMaximumRequestsPerPeriod(Long maximumRequestsPerPeriod) {
        this.maximumRequestsPerPeriod = maximumRequestsPerPeriod;
    }

    public long getTimePeriodMillis() {
        return timePeriodMillis;
    }

    public void setTimePeriodMillis(long timePeriodMillis) {
        this.timePeriodMillis = timePeriodMillis;
    }

    public List<InterceptorType> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<InterceptorType> interceptors) {
        this.interceptors = interceptors;
    }

    public List<ProcessorType> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ProcessorType> outputs) {
        this.outputs = outputs;
    }
}
