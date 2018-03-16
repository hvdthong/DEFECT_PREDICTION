package org.apache.camel.component.velocity;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.component.ResourceBasedEndpoint;
import org.apache.camel.util.ExchangeHelper;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

/**
 * @version $Revision: 1.1 $
 */
public class VelocityEndpoint extends ResourceBasedEndpoint {
    private final VelocityComponent component;
    private VelocityEngine velocityEngine;

    public VelocityEndpoint(String uri, VelocityComponent component, String resourceUri, Map parameters) {
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

    public VelocityEngine getVelocityEngine() throws Exception {
        if (velocityEngine == null) {
            velocityEngine = component.getVelocityEngine();
            velocityEngine.init();
        }
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    protected void onExchange(Exchange exchange) throws Exception {
        Reader reader = new InputStreamReader(getResource().getInputStream());
        StringWriter buffer = new StringWriter();
        String logTag = getClass().getName();
        Map variableMap = ExchangeHelper.createVariableMap(exchange);
        Context velocityContext = new VelocityContext(variableMap);
        VelocityEngine engine = getVelocityEngine();
        engine.evaluate(velocityContext, buffer, logTag, reader);

        Message out = exchange.getOut(true);
        out.setBody(buffer.toString());
        out.setHeader("org.apache.camel.velocity.resource", getResource());
    }
}
