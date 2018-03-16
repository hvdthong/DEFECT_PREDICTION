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
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.log.SimpleLog4JLogSystem;
import org.springframework.core.io.Resource;

/**
 * @version $Revision: 736047 $
 */
public class VelocityEndpoint extends ResourceBasedEndpoint {
    private final VelocityComponent component;
    private VelocityEngine velocityEngine;
    private boolean loaderCache = true;
    private String encoding;

    public VelocityEndpoint(String uri, VelocityComponent component, String resourceUri, Map parameters) {
        super(uri, component, resourceUri, null);
        this.component = component;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public ExchangePattern getExchangePattern() {
        return ExchangePattern.InOut;
    }

    private VelocityEngine getVelocityEngine() throws Exception {
        if (velocityEngine == null) {
            velocityEngine = component.getVelocityEngine();
            velocityEngine.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, isLoaderCache() ? Boolean.TRUE : Boolean.FALSE);
            velocityEngine.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, SimpleLog4JLogSystem.class.getName());
            velocityEngine.setProperty("runtime.log.logsystem.log4j.category", VelocityEndpoint.class.getName());
            velocityEngine.init();
        }
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public boolean isLoaderCache() {
        return loaderCache;
    }

    /**
     * Enables / disables the velocity resource loader cache which is enabled by default
     *
     * @param loaderCache a flag to enable/disable the cache
     */
    public void setLoaderCache(boolean loaderCache) {
        this.loaderCache = loaderCache;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onExchange(Exchange exchange) throws Exception {
        Resource resource = getResource();

        Reader reader = encoding != null ? new InputStreamReader(getResourceAsInputStream(), encoding) : new InputStreamReader(getResourceAsInputStream());
        StringWriter buffer = new StringWriter();
        String logTag = getClass().getName();
        Map variableMap = ExchangeHelper.createVariableMap(exchange);
        Context velocityContext = new VelocityContext(variableMap);

        VelocityEngine engine = getVelocityEngine();
        if (log.isDebugEnabled()) {
            log.debug("Velocity is evaluating using velocity context: " + variableMap);
        }
        engine.evaluate(velocityContext, buffer, logTag, reader);

        Message out = exchange.getOut(true);
        out.setBody(buffer.toString());
        out.setHeader("org.apache.camel.velocity.resource", resource);
        Map<String, Object> headers = (Map<String, Object>)velocityContext.get("headers");
        for (String key : headers.keySet()) {
            out.setHeader(key, headers.get(key));
        }
    }

}
