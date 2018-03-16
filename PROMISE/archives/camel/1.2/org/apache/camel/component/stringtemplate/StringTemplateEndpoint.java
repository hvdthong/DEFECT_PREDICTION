package org.apache.camel.component.stringtemplate;

import java.io.StringWriter;
import java.util.Map;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.component.ResourceBasedEndpoint;
import org.apache.camel.converter.IOConverter;
import org.apache.camel.util.ExchangeHelper;

/**
 * @version $Revision: 1.1 $
 */
public class StringTemplateEndpoint extends ResourceBasedEndpoint {
    private final StringTemplateComponent component;

    public StringTemplateEndpoint(String uri, StringTemplateComponent component, String resourceUri, Map parameters) {
        super(uri, component, resourceUri, null);
        this.component = component;
    }

    public boolean isSingleton() {
        return true;
    }

    @Override
    public ExchangePattern getExchangePattern() {
        return ExchangePattern.InOut;
    }

    @Override
    protected void onExchange(Exchange exchange) throws Exception {
        StringWriter buffer = new StringWriter();
        Map variableMap = ExchangeHelper.createVariableMap(exchange);

        String text = IOConverter.toString(getResource().getInputStream());
        StringTemplate template = new StringTemplate(text);
        template.setAttributes(variableMap);
        template.write(new AutoIndentWriter(buffer));

        Message out = exchange.getOut(true);
        out.setBody(buffer.toString());
        out.setHeader("org.apache.camel.stringtemplate.resource", getResource());
    }
}
