package org.apache.synapse.core.axis2;

import org.apache.synapse.config.SynapseConfigUtils;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.ws.commons.schema.resolver.URIResolver;
import org.xml.sax.InputSource;

/**
 * Class that adapts a {@link ResourceMap} to {@link URIResolver}.
 */
public class CustomURIResolver implements URIResolver {
    private ResourceMap resourceMap;
    private SynapseConfiguration synCfg;

    public CustomURIResolver() {
    }
    
    /**
     * Constructor.
     * 
     * @param resourceMap the resource map; may be null if no resource map is configured
     * @param synCfg the Synapse configuration
     */
    public CustomURIResolver(ResourceMap resourceMap,
                                  SynapseConfiguration synCfg) {
        this();
        this.resourceMap = resourceMap;
        this.synCfg = synCfg;
    }
    
    /**
     * Resolve a schema import.
     * This method will first attempt to resolve the location using the configured
     * {@link ResourceMap} object. If this fails (because no {@link ResourceMap} is
     * configured or because {@link ResourceMap#resolve(SynapseConfiguration, String)}
     * returns null, it will resolve the location using
     * {@link SynapseConfigUtils#resolveRelativeURI(String, String)}.
     */
    public InputSource resolveEntity(String targetNamespace, String schemaLocation, String baseUri) {
        InputSource result = null;
        if (resourceMap != null) {
            result = resourceMap.resolve(synCfg, schemaLocation);
        }
        if (result == null) {
            result = new InputSource(SynapseConfigUtils.resolveRelativeURI(baseUri, schemaLocation));
        }
        return result;
    }
}
