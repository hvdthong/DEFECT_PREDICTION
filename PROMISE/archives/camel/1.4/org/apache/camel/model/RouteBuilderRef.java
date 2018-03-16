package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.CamelContextHelper;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents an XML &lt;routeBuilderRef/&gt; element
 *
 * @version $Revision: 660266 $
 */
@XmlRootElement(name = "routeBuilderRef")
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteBuilderRef extends IdentifiedType {
    @XmlAttribute(required = true)
    private String ref;

    public RouteBuilderRef() {
    }

    public RouteBuilderRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "RouteBuilderRef[" + getRef() + "]";
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public RouteBuilder createRouteBuilder(CamelContext camelContext) {
        ObjectHelper.notNull(camelContext, "camelContext");
        ObjectHelper.notNull(ref, "ref");
        RouteBuilder builder = CamelContextHelper.lookup(camelContext, ref, RouteBuilder.class);
        return builder != null ? builder : CamelContextHelper.newInstance(camelContext, RouteBuilder.class);
    }
}
