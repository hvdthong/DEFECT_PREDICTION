package org.apache.camel.spring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.camel.model.IdentifiedType;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A Spring {@link FactoryBean} for creating a new {@link ProducerTemplate}
 * instance with a minimum of XML
 * 
 * @version $Revision: 663796 $
 */
@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.FIELD)
public class CamelTemplateFactoryBean extends IdentifiedType implements FactoryBean, InitializingBean, CamelContextAware {
    @XmlAttribute(required = false)
    private String defaultEndpoint;
    @XmlTransient
    private CamelContext camelContext;

    public void afterPropertiesSet() throws Exception {
        if (camelContext == null) {
            throw new IllegalArgumentException("A CamelContext must be injected!");
        }
    }

    public Object getObject() throws Exception {
        CamelContext context = getCamelContext();
        if (defaultEndpoint != null) {
            Endpoint endpoint = context.getEndpoint(defaultEndpoint);
            if (endpoint == null) {
                throw new IllegalArgumentException("No endpoint found for URI: " + defaultEndpoint);
            } else {
                return new DefaultProducerTemplate(context, endpoint);
            }
        }
        return new DefaultProducerTemplate(context);
    }

    public Class getObjectType() {
        return DefaultProducerTemplate.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public String getDefaultEndpoint() {
        return defaultEndpoint;
    }

    /**
     * Sets the default endpoint URI used by default for sending message
     * exchanges
     */
    public void setDefaultEndpoint(String defaultEndpoint) {
        this.defaultEndpoint = defaultEndpoint;
    }
}
