package org.apache.camel.component.freemarker;

import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.springframework.core.io.Resource;

/**
 * Freemarker component.
 */
public class FreemarkerComponent extends DefaultComponent {

    private Configuration configuraiton;
    private Configuration noCacheConfiguration;

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        FreemarkerEndpoint endpoint = new FreemarkerEndpoint(uri, this, remaining, parameters);

        Configuration config;
        boolean cache = (Boolean) getAndRemoveParameter(parameters, "contentCache", Boolean.class, Boolean.TRUE);
        if (cache) {
            config = getConfiguraiton();
        } else {
            config = getNoCacheConfiguration();
        }

        endpoint.setConfiguration(config);
        return endpoint;
    }

    public synchronized Configuration getConfiguraiton() {
        if (configuraiton == null) {
            configuraiton = new Configuration();
            configuraiton.setTemplateLoader(new ClassTemplateLoader(Resource.class, "/"));
        }
        return (Configuration) configuraiton.clone();
    }

    public void setConfiguraiton(Configuration configuraiton) {
        this.configuraiton = configuraiton;
    }

    private synchronized Configuration getNoCacheConfiguration() {
        if (noCacheConfiguration == null) {
            noCacheConfiguration = (Configuration) getConfiguraiton().clone();
            noCacheConfiguration.setCacheStorage(new NoCacheStorage());
        }
        return noCacheConfiguration;
    }

}
