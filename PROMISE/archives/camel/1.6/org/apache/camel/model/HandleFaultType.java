package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.processor.HandleFaultProcessor;

/**
 * Represents an XML &lt;handleFault/&gt; element
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "handleFault")
@XmlAccessorType(XmlAccessType.FIELD)
public class HandleFaultType extends InterceptorRef {

    public HandleFaultType() {
        super(new HandleFaultProcessor());
    }

    @Override
    public String getShortName() {
        return "handleFault";
    }

    @Override
    public String toString() {
        return "HandleFault[" + getLabel() + "]";
    }

}
