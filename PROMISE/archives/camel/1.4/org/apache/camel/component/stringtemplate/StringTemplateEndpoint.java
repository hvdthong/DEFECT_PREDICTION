package org.apache.camel.component.stringtemplate;

import java.io.StringWriter;
import java.util.Map;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.ResourceBasedEndpoint;
import org.apache.camel.converter.IOConverter;
import org.apache.camel.util.ExchangeHelper;

/**
 * @version $Revision: 663822 $
 */
public class StringTemplateEndpoint extends ResourceBasedEndpoint {

    public StringTemplateEndpoint(String uri, StringTemplateComponent component, String resourceUri, Map parameters) {
        super(uri, component, resourceUri, null);
    }

    public StringTemplateEndpoint(String endpointUri, Processor processor, String resourceUri) {
        super(endpointUri, processor, resourceUri);
    }

    @Override
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

        String text = IOConverter.toString(getResourceAsInputStream());
        StringTemplate template = new StringTemplate(text);
        template.setAttributes(variableMap);
        if (log.isDebugEnabled()) {
            log.debug("StringTemplate is writing using attributes: " + variableMap);
        }
        template.write(new AutoIndentWriter(buffer));

        Message out = exchange.getOut(true);
        out.setBody(buffer.toString());
        out.setHeader("org.apache.camel.stringtemplate.resource", getResource());
    }
    
}
