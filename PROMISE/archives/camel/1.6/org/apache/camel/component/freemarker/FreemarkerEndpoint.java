package org.apache.camel.component.freemarker;

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.component.ResourceBasedEndpoint;
import org.apache.camel.util.ExchangeHelper;
import org.apache.camel.util.ObjectHelper;

/**
 * Freemarker endpoint
 */
public class FreemarkerEndpoint extends ResourceBasedEndpoint {

    private String encoding;
    private Configuration configuration;

    public FreemarkerEndpoint(String uri, Component component, String resourceUri, Map parameters) {
        super(uri, component, resourceUri, null);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public ExchangePattern getExchangePattern() {
        return ExchangePattern.InOut;
    }

    /**
     * Sets the encoding to be used for loading the template file.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the Freemarker configuration to use
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onExchange(Exchange exchange) throws Exception {
        String path = getResourceUri();
        ObjectHelper.notNull(configuration, "configuration");
        ObjectHelper.notNull(path, "resourceUri");

        Map variableMap = ExchangeHelper.createVariableMap(exchange);

        if (log.isDebugEnabled()) {
            log.debug("Freemarker is evaluating " + path + " using context: " + variableMap);
        }

        Template template;
        if (encoding != null) {
            template = configuration.getTemplate(path, encoding);
        } else {
            template = configuration.getTemplate(path);
        }
        StringWriter buffer = new StringWriter();
        template.process(variableMap, buffer);
        buffer.flush();

        Message out = exchange.getOut(true);
        out.setBody(buffer.toString());
        out.setHeader("org.apache.camel.freemarker.resource", getResource());
        out.setHeader("org.apache.camel.freemarker.resourceUri", path);
        Map<String, Object> headers = (Map<String, Object>) variableMap.get("headers");
        for (String key : headers.keySet()) {
            out.setHeader(key, headers.get(key));
        }
    }

}

