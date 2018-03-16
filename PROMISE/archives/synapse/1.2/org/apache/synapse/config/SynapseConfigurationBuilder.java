package org.apache.synapse.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.util.DataSourceRegistrar;
import org.apache.synapse.config.xml.XMLConfigurationBuilder;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.synapse.mediators.builtin.DropMediator;
import org.apache.synapse.mediators.builtin.LogMediator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Builds a Synapse Configuration model with a given input
 * (e.g. XML, programmatic creation, default etc)
 */
public class SynapseConfigurationBuilder {

    private static Log log = LogFactory.getLog(SynapseConfigurationBuilder.class);

    /**
     * Return the default Synapse Configuration
     * @return the default configuration to be used
     */
    public static SynapseConfiguration getDefaultConfiguration() {
        SynapseConfiguration config = new SynapseConfiguration();
        SequenceMediator mainmediator = new SequenceMediator();
        mainmediator.addChild(new LogMediator());
        mainmediator.addChild(new DropMediator());
        config.addSequence(SynapseConstants.MAIN_SEQUENCE_KEY, mainmediator);
        SequenceMediator faultmediator = new SequenceMediator();
        LogMediator fault = new LogMediator();
        fault.setLogLevel(LogMediator.FULL);
        faultmediator.addChild(fault);
        config.addSequence(SynapseConstants.FAULT_SEQUENCE_KEY, faultmediator);
        return config;
    }

    /**
     * Build a Synapse configuration from a given XML configuration file
     *
     * @param configFile the XML configuration
     * @return the Synapse configuration model
     */
    public static SynapseConfiguration getConfiguration(String configFile) {

        try {
            Properties synapseProperties = loadSynapseProperties();
            DataSourceRegistrar.registerDataSources(synapseProperties);
            SynapseConfiguration synCfg
                    = XMLConfigurationBuilder.getConfiguration(new FileInputStream(configFile));
            log.info("Loaded Synapse configuration from : " + configFile);
            synCfg.setPathToConfigFile(new File(configFile).getAbsolutePath());
            synCfg.setProperties(synapseProperties);
            return synCfg;

        } catch (FileNotFoundException fnf) {
            handleException("Cannot load Synapse configuration from : " + configFile, fnf);
        } catch (Exception e) {
            handleException("Could not initialize Synapse : " + e.getMessage(), e);
        }
        return null;
    }

    private static Properties loadSynapseProperties() {

        try {
            Properties properties = new Properties();
            ClassLoader cl = Thread.currentThread().getContextClassLoader();

            if (log.isDebugEnabled()) {
                log.debug("synapse.properties file is loading from classpath");
            }

            InputStream in = cl.getResourceAsStream(SynapseConstants.SYNAPSE_PROPERTIES);
            if (in == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to load synapse.propeties file");
                }

                String path = SynapseConstants.CONF_DIRECTORY +
                        File.separatorChar + SynapseConstants.SYNAPSE_PROPERTIES;
                if (log.isDebugEnabled()) {
                    log.debug("synapse.properties file is loading from classpath" +
                            " with resource path '" + path + " '");
                }

                in = cl.getResourceAsStream(path);
                if (in == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Unable to load the synapse.properties file from classpath" +
                                " with resource name '" + path + " '");
                    }
                }
            }

            if (in != null) {
                properties.load(in);
            }
            
            return properties;

        } catch (Exception e) {
            log.info("Using the default tuning parameters for Synapse");
        }
        return new Properties();
    }

    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }
}
