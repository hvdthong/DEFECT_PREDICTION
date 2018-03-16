package org.apache.camel.component.xslt;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.builder.xml.XsltBuilder;
import org.apache.camel.component.ResourceBasedComponent;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.camel.impl.ProcessorEndpoint;
import org.springframework.core.io.Resource;

/**
 * for performing XSLT transforms of messages
 *
 * @version $Revision: 655776 $
 */
public class XsltComponent extends ResourceBasedComponent {
    private XmlConverter xmlConverter;

    public XmlConverter getXmlConverter() {
        return xmlConverter;
    }

    public void setXmlConverter(XmlConverter xmlConverter) {
        this.xmlConverter = xmlConverter;
    }

    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        Resource resource = resolveMandatoryResource(remaining);
        if (log.isDebugEnabled()) {
            log.debug(this + " using schema resource: " + resource);
        }
        XsltBuilder xslt = newInstance(XsltBuilder.class);

        XmlConverter converter = null;
        String converterName = getAndRemoveParameter(parameters, "converter", String.class);
        if (converterName != null) {
            converter = mandatoryLookup(converterName, XmlConverter.class);
        }
        if (converter == null) {
            converter = getXmlConverter();
        }
        if (converter != null) {
            xslt.setConverter(converter);
        }

        xslt.setTransformerInputStream(resource.getInputStream());
        configureXslt(xslt, uri, remaining, parameters);
        return new ProcessorEndpoint(uri, this, xslt);
    }

    protected void configureXslt(XsltBuilder xslt, String uri, String remaining, Map parameters) throws Exception {
        setProperties(xslt, parameters);
    }
}
