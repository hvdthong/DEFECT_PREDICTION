package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Processor;
import org.apache.camel.spi.Policy;
import org.apache.camel.spi.RouteContext;

/**
 * Represents an XML &lt;policy/&gt; element
 *
 * @version $Revision: 705880 $
 */
@XmlRootElement(name = "policy")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyRef extends OutputType<ProcessorType> {
    @XmlAttribute(required = true)
    private String ref;
    @XmlTransient
    private Policy policy;

    public PolicyRef() {
    }

    public PolicyRef(Policy policy) {
        this.policy = policy;
    }

    @Override
    public String toString() {
        return "Policy[" + description() + "]";
    }

    @Override
    public String getShortName() {
        return "policy";
    }

    @Override
    public String getLabel() {
        if (ref != null) {
            return "ref:  " + ref;
        } else if (policy != null) {
            return policy.toString();
        } else {
            return "";
        }
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = createOutputsProcessor(routeContext);

        Policy policy = resolvePolicy(routeContext);
        if (policy == null) {
            throw new IllegalArgumentException("No policy configured: " + this);
        }
        return policy.wrap(childProcessor);
    }

    protected Policy resolvePolicy(RouteContext routeContext) {
        if (policy == null) {
            policy = routeContext.lookup(getRef(), Policy.class);
        }
        return policy;
    }

    protected String description() {
        if (policy != null) {
            return policy.toString();
        } else {
            return "ref: " + ref;
        }
    }
}
