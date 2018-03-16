package org.apache.synapse.registry;

import org.apache.axiom.om.OMNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.Entry;
import org.apache.synapse.config.XMLToObjectMapper;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.mediators.base.SequenceMediator;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Implements the core Registry lookup algorithm
 */
public abstract class AbstractRegistry implements Registry {

    private static final Log log = LogFactory.getLog(AbstractRegistry.class);

    /** The list of configuration properties */
    protected Properties properties = new Properties();

    /**
     * Get the resource for the given key from this registry
     * @param entry
     * @return the matching resultant object
     */
    public Object getResource(Entry entry) {

        OMNode omNode = null;
        RegistryEntry re = null;


        if (entry.isCached() && !entry.isExpired()) {
            return entry.getValue();

        } else if (!entry.isCached()) {
            omNode = lookup(entry.getKey());
            if (omNode == null) {
                return null;
            } else {
                re = getRegistryEntry(entry.getKey());
            }

        } else if (entry.isExpired()) {
            if (log.isDebugEnabled()) {
                log.debug("Cached object has expired for key : " + entry.getKey());
            }
            re = getRegistryEntry(entry.getKey());

            if (re.getVersion() != Long.MIN_VALUE &&
                re.getVersion() == entry.getVersion()) {
                if (log.isDebugEnabled()) {
                    log.debug("Expired version number is same as current version in registry");
                }

                if (re.getCachableDuration() > 0) {
                    entry.setExpiryTime(
                            System.currentTimeMillis() + re.getCachableDuration());
                } else {
                    entry.setExpiryTime(-1);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Renew cache lease for another " + re.getCachableDuration() / 1000 + "s");
                }

                return entry.getValue();

            } else {
                omNode = lookup(entry.getKey());
            }
        }


        if (entry.getMapper() != null) {
            entry.setValue(entry.getMapper().getObjectFromOMNode(omNode));

            if (entry.getValue() instanceof SequenceMediator) {
                SequenceMediator seq = (SequenceMediator) entry.getValue();
                seq.setDynamic(true);
                seq.setRegistryKey(entry.getKey());
            } else if (entry.getValue() instanceof Endpoint) {
                Endpoint ep = (Endpoint) entry.getValue();
            }

        } else {
            if (re != null && re.getType() != null) {

                XMLToObjectMapper mapper = getMapper(re.getType());
                if (mapper != null) {
                    entry.setMapper(mapper);
                    entry.setValue(mapper.getObjectFromOMNode(omNode));

                } else {
                    entry.setValue(omNode);
                }
            }
        }

        if (re != null) {
            if (re.getCachableDuration() > 0) {
                entry.setExpiryTime(System.currentTimeMillis() + re.getCachableDuration());
            } else {
                entry.setExpiryTime(-1);
            }
            entry.setVersion(re.getVersion());
        }

        return entry.getValue();
    }

    private XMLToObjectMapper getMapper(URI type) {
        return null;
    }

    public String getProviderClass() {
        return this.getClass().getName();
    }

    public Properties getConfigurationProperties() {
        return properties;
    }

    public void init(Properties properties) {
        this.properties =properties;
    }
}
