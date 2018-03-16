package org.apache.synapse.core.axis2;

import org.apache.synapse.config.SynapseConfigUtils;
import org.apache.synapse.config.SynapseConfiguration;
import org.xml.sax.InputSource;

import javax.wsdl.xml.WSDLLocator;

/**
 * Class that adapts a {@link ResourceMap} object to {@link WSDLLocator}.
 */
public class CustomWSDLLocator implements WSDLLocator {
    private final InputSource baseInputSource;
    private final String baseURI;
    private ResourceMap resourceMap;
    private SynapseConfiguration synCfg;

    private String latestImportURI;

    public CustomWSDLLocator(InputSource baseInputSource,
                                  String baseURI) {
        this.baseInputSource = baseInputSource;
        this.baseURI = baseURI;
    }

    public CustomWSDLLocator(InputSource baseInputSource,
                                  String baseURI,
                                  ResourceMap resourceMap,
                                  SynapseConfiguration synCfg) {
        this(baseInputSource, baseURI);
        this.resourceMap = resourceMap;
        this.synCfg = synCfg;
    }

    public InputSource getBaseInputSource() {
        return baseInputSource;
    }

    public String getBaseURI() {
        return baseURI;
    }

    /**
     * Resolve a schema or WSDL import.
     * This method will first attempt to resolve the location using the configured
     * {@link ResourceMap} object. If this fails (because no {@link ResourceMap} is
     * configured or because {@link ResourceMap#resolve(SynapseConfiguration, String)}
     * returns null, it will resolve the location using
     * {@link SynapseConfigUtils#resolveRelativeURI(String, String)}.
     */
    public InputSource getImportInputSource(String parentLocation, String relativeLocation) {
        InputSource result = null;
        if (resourceMap != null) {
            result = resourceMap.resolve(synCfg, relativeLocation);
        }
        if (result == null) {
            result = new InputSource(SynapseConfigUtils.resolveRelativeURI(parentLocation, relativeLocation));
        }
        this.latestImportURI = relativeLocation;
        return result;
    }

    public String getLatestImportURI() {
        return latestImportURI;
    }

    public void close() {
    }
}
