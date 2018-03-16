package org.apache.camel.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A useful base class for output types
 *
 * @version $Revision: 640438 $
 */
@XmlType(name = "outputType")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutputType<Type extends ProcessorType> extends ProcessorType<Type> {
    private static final transient Log LOG = LogFactory.getLog(OutputType.class);

    @XmlElementRef
    protected List<ProcessorType<?>> outputs = new ArrayList<ProcessorType<?>>();

    public List<ProcessorType<?>> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ProcessorType<?>> outputs) {
        this.outputs = outputs;
        if (outputs != null) {
            for (ProcessorType output : outputs) {
                configureChild(output);
            }
        }
    }


    @Override
    protected void configureChild(ProcessorType output) {
        super.configureChild(output);
        if (isInheritErrorHandler()) {
            output.setErrorHandlerBuilder(getErrorHandlerBuilder());
        }
/*
        List<InterceptorType> list = output.getInterceptors();
        if (list == null) {
            log.warn("No interceptor collection: " + output);
        }
        else {
            list.addAll(getInterceptors());
        }
*/
    }
}
